/**
 *2017年10月12日 下午1:39:48
 */
package com.cntinker.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.Pivot;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;

/**
 * @author: liubin
 *
 */
public class JsqlParserHelper {

	public static String build_select_table(String sql, List<String> str_table)
			throws JSQLParserException {
		CCJSqlParserManager parserManager = new CCJSqlParserManager();
		Select select = (Select) parserManager.parse(new StringReader(sql));
		PlainSelect plain = (PlainSelect) select.getSelectBody();
		plain.setFromItem(null);
		plain.setJoins(null);
		List<Join> select_join = new ArrayList<Join>();

		if (str_table.size() == 1) {
			plain.setFromItem(new Table(str_table.get(0)));
		}
		for (int i = 0; i < str_table.size() - 1 && str_table.size() >= 2;) { // no
																				// i++
			Join obj_join = new Join();
			if (i == 0)
				plain.setFromItem(new Table(str_table.get(0)));
			obj_join.setSimple(true); // 假定所有表的连接均为，——内连接
			i++;
			obj_join.setRightItem(new Table(str_table.get(i)));
			select_join.add(obj_join);
		}
		plain.setJoins(select_join);
		// plain.setForUpdateTable(new Table(str_table));
		return select.toString();
	}

	public static String build_insert_table(String sql, String str_table)
			throws JSQLParserException {
		Statement statement = CCJSqlParserUtil.parse(sql);
		Insert insertStatement = (Insert) statement;
		insertStatement.setTable(new Table(str_table));
		return insertStatement.toString();
	}

	public static String build_update_table(String sql, List<String> str_table)
			throws JSQLParserException {
		Statement statement = CCJSqlParserUtil.parse(sql);
		Update updateStatement = (Update) statement;
		List<Table> update_table = new ArrayList<Table>();
		for (int i = 0; i < str_table.size(); i++) {
			Table tabletest = new Table(); // string to Table
			tabletest.setName(str_table.get(i));
			update_table.add(tabletest);
		}
		updateStatement.setTables(update_table);
		return updateStatement.toString();

	}

	public static String changeSelect(String sql, List<String> tables)
			throws JSQLParserException {
		Select select = (Select) CCJSqlParserUtil.parse(sql);
		StringBuilder buffer = new StringBuilder();
		ExpressionDeParser expressionDeParser = new ExpressionDeParser();
		SelectDeParser deparser = new MySelectDeParser(expressionDeParser,
				buffer, tables);
		expressionDeParser.setSelectVisitor(deparser);
		expressionDeParser.setBuffer(buffer);
		select.getSelectBody().accept(deparser);

		return buffer.toString();
	}

	public static void main(String[] args) throws Exception {
		String updateSql = "update table_a set value='123'";
		List<String> updateTable = new ArrayList<String>();
		updateTable.add("table_b");

		System.out.println("old update: " + updateSql);
		System.out.println("new update: "
				+ build_update_table(updateSql, updateTable));

		// String selectSql =
		// "SELECT a.* FROM a JOIN b ON (a.id = b.id AND a.department = b.department)";
		String selectSql = "select * from books as t1 left join hbooks as t2 on t1.id=t2.id";
		List<String> selectTable = new ArrayList<String>();
		selectTable.add("a_change");
		selectTable.add("b_change");

		System.out.println("old select: " + selectSql);
		System.out.println("new select: "
				+ changeSelect(selectSql, selectTable));
	}

	static class MySelectDeParser extends SelectDeParser {
		private List<String> changeTables;

		public MySelectDeParser(ExpressionVisitor expressionVisitor,
				StringBuilder buffer, List<String> changeTables) {
			super(expressionVisitor, buffer);
			this.changeTables = changeTables;
		}

		@Override
		public void visit(Table tableName) {
			System.out.println("tableName:::: " + tableName);
			String schema = tableName.getSchemaName();
			if (!StringUtils.isEmpty(schema))
				tableName.setSchemaName("mysql" + "." + schema);
			StringBuilder buffer = getBuffer();
			buffer.append(tableName.getFullyQualifiedName());
			Pivot pivot = tableName.getPivot();
			if (pivot != null) {
				pivot.accept(this);
			}
			Alias alias = tableName.getAlias();
			if (alias != null) {
				buffer.append(alias);
			}
		}

	}
}
