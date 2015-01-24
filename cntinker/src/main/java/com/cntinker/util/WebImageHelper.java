/**
 *2012-5-21 下午4:21:25
 */
package com.cntinker.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: bin_liu
 * 
 */
public class WebImageHelper {

	public static String getValiCode() {
		return "validateCode";
	}

	private static Color getRandColor(Random random, int fc, int bc) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		random = null;
		return new Color(r, g, b);
	}

	public static String getGBK(int nlen) {
		StringBuffer _b = new StringBuffer();
		int length = "的一了是我不在人们有来他这上着个地到大里说就去子得也和那要下看天时过出小么起你都把好还多没为又可家学只以主会样年想生同老中十从自面前头道它后然走很像见两用她国动进成回什边作对开而己些现山民候经发工向事命给长水几义三声于高手知理眼志点心战二问但身方实吃做叫当住听革打呢真全才四已所敌之最光产情路分总条白话东席次亲如被花口放儿常气五第使写军吧文运再果怎定许快明行因别飞外树物活部门无往船望新带队先力完却站代员机更九您每风级跟笑啊孩万少直意夜比阶连车重便斗马哪化太指变社似士者干石满日决百原拿群究各六本思解立河村八难早论吗根共让相研今其书坐接应关信觉步反处记将千找争领或师结块跑谁草越字加脚紧爱等习阵怕月青半火法题建赶位唱海七女任件感准张团屋离色脸片科倒睛利世刚且由送切星导晚表够整认响雪流未场该并底深刻平伟忙提确近亮轻讲农古黑告界拉名呀土清阳照办史改历转画造嘴此治北必服雨穿内识验传业菜爬睡兴形量咱观苦体众通冲合破友度术饭公旁房极南枪读沙岁线野坚空收算至政城劳落钱特围弟胜教热展包歌类渐强数乡呼性音答哥际旧神座章帮啦受系令跳非何牛取入岸敢掉忽种装顶急林停息句区衣般报叶压慢叔背细"
				.length();
		Random random = new Random();
		for (int i = 0; i < nlen; ++i) {
			int start = random.nextInt(length);
			_b.append("的一了是我不在人们有来他这上着个地到大里说就去子得也和那要下看天时过出小么起你都把好还多没为又可家学只以主会样年想生同老中十从自面前头道它后然走很像见两用她国动进成回什边作对开而己些现山民候经发工向事命给长水几义三声于高手知理眼志点心战二问但身方实吃做叫当住听革打呢真全才四已所敌之最光产情路分总条白话东席次亲如被花口放儿常气五第使写军吧文运再果怎定许快明行因别飞外树物活部门无往船望新带队先力完却站代员机更九您每风级跟笑啊孩万少直意夜比阶连车重便斗马哪化太指变社似士者干石满日决百原拿群究各六本思解立河村八难早论吗根共让相研今其书坐接应关信觉步反处记将千找争领或师结块跑谁草越字加脚紧爱等习阵怕月青半火法题建赶位唱海七女任件感准张团屋离色脸片科倒睛利世刚且由送切星导晚表够整认响雪流未场该并底深刻平伟忙提确近亮轻讲农古黑告界拉名呀土清阳照办史改历转画造嘴此治北必服雨穿内识验传业菜爬睡兴形量咱观苦体众通冲合破友度术饭公旁房极南枪读沙岁线野坚空收算至政城劳落钱特围弟胜教热展包歌类渐强数乡呼性音答哥际旧神座章帮啦受系令跳非何牛取入岸敢掉忽种装顶急林停息句区衣般报叶压慢叔背细"
					.substring(start, start + 1));
		}
		return _b.toString();
	}

	public static String getAscii(int nlen) {
		StringBuffer _b = new StringBuffer();
		int _atmp = 0;
		Random random = new Random();
		for (int i = 0; i < nlen; ++i) {
			_atmp = random.nextInt(128);
			if (((_atmp >= 65) && (_atmp <= 90))
					|| ((_atmp >= 97) && (_atmp <= 122)))
				_b.append((char) _atmp);
			else {
				_b.append(_atmp % 10);
			}
		}
		return _b.toString();
	}

	public static String getNumber(int nlen) {
		StringBuffer _b = new StringBuffer();
		int _atmp = 0;
		Random random = new Random();
		for (int i = 0; i < nlen; ++i) {
			_atmp = random.nextInt(10);
			_b.append(_atmp);
		}
		return _b.toString();
	}

	private static void setLine(Graphics g, Random random, int width,
			int height, int fontsize) {
		int nline = random.nextInt(7);
		int ndraw = 8;
		int ndrawRect = 1;
		if (fontsize > 20) {
			ndrawRect += (fontsize - 10) / 10;
		}
		int ndrawx = width / ndraw;
		int ndrawy = height / ndraw;
		int ndrawmovx = (width - ndrawx * ndraw) / 2;
		int ndrawmovy = (height - ndrawy * ndraw) / 2;
		switch (nline) {
		case 1:
			for (int x = 1; x < ndrawx; ++x) {
				g.setColor(getRandColor(random, 100, 200));
				for (int y = 0; y < height; ++y)
					g.fillRect(ndrawmovx + x * ndraw, y, ndrawRect, ndrawRect);
			}
			for (int y = 1; y < ndrawy; ++y) {
				g.setColor(getRandColor(random, 100, 200));
				for (int x = 0; x < width; ++x)
					g.fillRect(x, ndrawmovy + y * ndraw, ndrawRect, ndrawRect);
			}
			for (int x = 0; x < ndrawx; ++x) {
				g.setColor(getRandColor(random, 100, 200));
				for (int y = 0; y < height; ++y) {
					if (x * ndraw + y < width)
						g.fillRect(x * ndraw + y, y, ndrawRect, ndrawRect);
					if (x * ndraw - y > 0)
						g.fillRect(x * ndraw - y, y, ndrawRect, ndrawRect);
				}
			}
			for (int y = 0; y < ndrawy; ++y) {
				g.setColor(getRandColor(random, 100, 200));
				for (int x = 0; x < width; ++x) {
					if (y * ndraw + x < height)
						g.fillRect(x, y * ndraw + x, ndrawRect, ndrawRect);
					if (y * ndraw - x > 0)
						g.fillRect(x, y * ndraw - x, ndrawRect, ndrawRect);
				}
			}
			break;
		case 2:
			for (int i = 0; i < 155 * (width / 100 + 1 + height / 25); ++i) {
				g.setColor(getRandColor(random, 150, 200));
				int x = random.nextInt(width);
				int y = random.nextInt(height);
				int xl = random.nextInt(20);
				int yl = random.nextInt(20);
				for (int x2 = 0; x < xl; ++x)
					for (int y2 = 0; y2 < yl; ++y2)
						g.fillRect(x + x2, y + y2, ndrawRect, ndrawRect);
			}
		case 3:
			for (int x = 1; x < ndrawx; ++x) {
				g.setColor(getRandColor(random, 130, 200));
				for (int y = 0; y < height; ++y)
					g.fillRect(ndrawmovx + x * ndraw, y, ndrawRect, ndrawRect);
			}
			for (int y = 1; y < ndrawy; ++y) {
				g.setColor(getRandColor(random, 130, 200));
				for (int x = 0; x < width; ++x)
					g.fillRect(x, ndrawmovy + y * ndraw, ndrawRect, ndrawRect);
			}
			for (int i = 0; i < 155 * (width / 100 + 1 + height / 25); ++i) {
				g.setColor(getRandColor(random, 150, 200));
				int x = random.nextInt(width);
				int y = random.nextInt(height);
				int xl = random.nextInt(20);
				int yl = random.nextInt(20);
				g.drawLine(x, y, x + xl, y + yl);
			}
			break;
		case 4:
			for (int x = 1; x < width; x += 9) {
				for (int y = 0; y < height; y += 7) {
					g.setColor(getRandColor(random, 100, 220));
					g.fillRect(x, y, random.nextInt(6) + 1,
							random.nextInt(4) + 1);
				}
			}
			break;
		case 5:
			for (int x = 1; x < width; ++x) {
				for (int y = 0; y < height; ++y) {
					g.setColor(getRandColor(random, 95, 225));
					g.fillRect(x, y, 4, 6);
				}
			}
			break;
		case 6:
			for (int x = 1; x < width; x += 12) {
				for (int y = 0; y < height; y += 6) {
					g.setColor(getRandColor(random, 100, 220));
					g.fillRect(x, y, 10, random.nextInt(3) + 1);
				}
			}
			break;
		default:
			for (int i = 0; i < 155 * (width / 100 + 1 + height / 25); ++i) {
				g.setColor(getRandColor(random, 150, 200));
				int x = random.nextInt(width);
				int y = random.nextInt(height);
				int xl = random.nextInt(20);
				int yl = random.nextInt(20);
				g.drawLine(x, y, x + xl, y + yl);
			}
		}
		g = null;
		random = null;
	}

	public static boolean createJpg(HttpServletRequest request,
			OutputStream out, String imagefile, String writeText, int textX,
			int textY, int fontsize, int red, int green, int blue)
			throws Exception {
		try {
			File _file = new File(imagefile);
			if (_file.exists()) {
				Image src = ImageIO.read(_file);
				int wideth = src.getWidth(null);
				int height = src.getHeight(null);

				BufferedImage image = new BufferedImage(wideth, height, 1);

				Graphics g = image.getGraphics();
				g.drawImage(src, 0, 0, wideth, height, null);
				g.setFont(new Font("Times New Roman", 1, fontsize));
				if ((textY == -1) && (textX == -1)) {
					textY = height / 2 - 20;
					textX = wideth / 2 - writeText.getBytes().length * 8;
				} else {
					textY = height / 2 - 20 + textY;
					textX = wideth / 2 - writeText.getBytes().length * 8
							+ textX;
				}
				if ((red < 0) || (blue < 0) || (green < 0))
					g.setColor(getRandColor(new Random(), 160, 240));
				else
					g.setColor(new Color(red % 256, green % 256, blue % 256));
				g.drawString(writeText, textX, textY);
				g.dispose();

				ImageIO.write(image, "JPEG", out);
				return true;
			}
		} catch (Exception e) {
			throw e;
		}
		return false;
	}

	public static String createJpg(HttpServletRequest request,
			OutputStream out, int width, int height, String drawStr)
			throws Exception {
		return createJpg(request, out, width, height, drawStr, 16);
	}

	public static String createJpg(HttpServletRequest request,
			OutputStream out, int width, int height, String drawStr,
			int fontsize) throws Exception {
		return createJpg(request, out, width, height, 0, 0, drawStr, fontsize);
	}

	public static String createJpg(HttpServletRequest request,
			OutputStream out, int width, int height, int x, int y,
			String drawStr, int fontsize) throws Exception {
		try {
			request.getSession().setAttribute("validateCode", drawStr);

			BufferedImage image = new BufferedImage(width, height, 1);

			Graphics g = image.getGraphics();

			Random random = new Random();

			g.setColor(getRandColor(random, 200, 250));
			g.fillRect(0, 0, width, height);
			g.setFont(new Font("Times New Roman", 0, fontsize));

			setLine(g, random, width, height, fontsize);

			String[] fontTypes = { "宋体", "新宋体", "黑体", "楷体", "隶书", "Arial",
					"Times New Roman", "Verdana" };
			int fontTypesLength = fontTypes.length;
			int _clen = 1;
			int ma = 6;
			for (int i = 0; i < drawStr.length(); ++i) {
				g.setColor(new Color(20 + random.nextInt(110), 20 + random
						.nextInt(110), 20 + random.nextInt(110)));

				g.setFont(new Font(fontTypes[random.nextInt(fontTypesLength)],
						1, fontsize + random.nextInt(6)));

				String _tmp = drawStr.substring(i, i + 1);
				_clen = _tmp.getBytes().length;
				g.drawString(_tmp, ma + x, 19 + random.nextInt(6) - 3 + y);
				if (fontsize < 30)
					ma += 15 * ((_clen > 1) ? 2 : 1);
				else
					ma += ((int) (16.0D * (1.0D + 0.5D * ((fontsize - 30) / 10 + 1))) - 1)
							* ((_clen > 1) ? 2 : 1);
			}
			g.dispose();

			ImageIO.write(image, "JPEG", out);
			g = null;
			random = null;
			image = null;
			return drawStr;
		} catch (Exception e) {
			throw e;
		}
	}

	private static String createWebJpg(HttpServletRequest request,
			HttpServletResponse response, int width, int height, int x, int y,
			int nlen, int types, String drawStr, int fontsize) throws Exception {
//		response.setContentType("image/jpeg");
//		response.setHeader("Pragma", "No-cache");
//		response.setHeader("Cache-Control", "no-cache");
//		response.setDateHeader("Expires", 0L);
		try {
			if (drawStr == null) {
				switch (types) {
				case 1:
					drawStr = getAscii(nlen);
					break;
				case 2:
					drawStr = getGBK(nlen);
					break;
				default:
					drawStr = getNumber(nlen);
				}
			}
			createJpg(request, response.getOutputStream(), width, height, x, y,
					drawStr, fontsize);
			return drawStr;
		} catch (Exception e) {
			throw e;
		}
	}

	public static String createNumber2Jpg(HttpServletRequest request,
			HttpServletResponse response, int width, int height, int nlen)
			throws Exception {
		return createWebJpg(request, response, width, height, 0, 0, nlen, 0,
				null, 16);
	}

	public static String createNumber2Jpg(HttpServletRequest request,
			HttpServletResponse response, int width, int height, int nlen,
			int nfontsize) throws Exception {
		return createWebJpg(request, response, width, height, 0, 0, nlen, 0,
				null, nfontsize);
	}

	public static String createAscii2Jpg(HttpServletRequest request,
			HttpServletResponse response, int width, int height, int nlen)
			throws Exception {
		return createWebJpg(request, response, width, height, 0, 0, nlen, 1,
				null, 16);
	}

	public static String createAscii2Jpg(HttpServletRequest request,
			HttpServletResponse response, int width, int height, int nlen,
			int nfontsize) throws Exception {
		return createWebJpg(request, response, width, height, 0, 0, nlen, 1,
				null, nfontsize);
	}

	public static String createGBK2Jpg(HttpServletRequest request,
			HttpServletResponse response, int width, int height, int nlen)
			throws Exception {
		return createWebJpg(request, response, width, height, 0, 0, nlen, 2,
				null, 16);
	}

	public static String createGBK2Jpg(HttpServletRequest request,
			HttpServletResponse response, int width, int height, int nlen,
			int nfontsize) throws Exception {
		return createWebJpg(request, response, width, height, 0, 0, nlen, 2,
				null, nfontsize);
	}

	public static String createNumber2Jpg(HttpServletRequest request,
			OutputStream out, int width, int height, int nlen) throws Exception {
		return createJpg(request, out, width, height, getNumber(nlen));
	}

	public static String createNumber2Jpg(HttpServletRequest request,
			OutputStream out, int width, int height, int nlen, int nfontsize)
			throws Exception {
		return createJpg(request, out, width, height, getNumber(nlen),
				nfontsize);
	}

	public static String createAscii2Jpg(HttpServletRequest request,
			OutputStream out, int width, int height, int nlen) throws Exception {
		return createJpg(request, out, width, height, getAscii(nlen));
	}

	public static String createAscii2Jpg(HttpServletRequest request,
			OutputStream out, int width, int height, int nlen, int nfontsize)
			throws Exception {
		return createJpg(request, out, width, height, getAscii(nlen), nfontsize);
	}

	public static String createGBK2Jpg(HttpServletRequest request,
			OutputStream out, int width, int height, int nlen) throws Exception {
		return createJpg(request, out, width, height, getGBK(nlen));
	}

	public static String createGBK2Jpg(HttpServletRequest request,
			OutputStream out, int width, int height, int nlen, int nfontsize)
			throws Exception {
		return createJpg(request, out, width, height, getGBK(nlen), nfontsize);
	}

	public static String createNumber2Jpg(HttpServletRequest request,
			HttpServletResponse response, int width, int height)
			throws Exception {
		return createWebJpg(request, response, width, height, 0, 0, 6, 0, null,
				16);
	}

	public static String createAscii2Jpg(HttpServletRequest request,
			HttpServletResponse response, int nlen) throws Exception {
		return createWebJpg(request, response, nlen * 16, 24, 0, 0, nlen, 1,
				null, 16);
	}

	public static String createAscii2Jpg(HttpServletRequest request,
			HttpServletResponse response, int width, int height)
			throws Exception {
		return createWebJpg(request, response, width, height, 0, 0, 6, 1, null,
				16);
	}

	public static String createGBK2Jpg(HttpServletRequest request,
			HttpServletResponse response, int nlen) throws Exception {
		return createWebJpg(request, response, nlen * 2 * 16, 24, 0, 0, nlen,
				2, null, 16);
	}

	public static String createGBK2Jpg(HttpServletRequest request,
			HttpServletResponse response, int width, int height)
			throws Exception {
		return createWebJpg(request, response, width, height, 0, 0, 6, 2, null,
				16);
	}

	public static String createNumber2Jpg(HttpServletRequest request,
			OutputStream out, int width, int height) throws Exception {
		return createJpg(request, out, width, height, getNumber(6));
	}

	public static String createAscii2Jpg(HttpServletRequest request,
			OutputStream out, int nlen) throws Exception {
		return createJpg(request, out, nlen * 16, 24, getAscii(nlen));
	}

	public static String createAscii2Jpg(HttpServletRequest request,
			OutputStream out, int width, int height) throws Exception {
		return createJpg(request, out, width, height, getAscii(6));
	}

	public static String createGBK2Jpg(HttpServletRequest request,
			OutputStream out, int width, int height) throws Exception {
		return createJpg(request, out, width, height, getGBK(6));
	}

	public static String createNumber2Jpg(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return createWebJpg(request, response, 100, 24, 0, 0, 6, 0,
				getNumber(6), 16);
	}

	public static String createNumber2Jpg(HttpServletRequest request,
			HttpServletResponse response, int nlen) throws Exception {
		return createWebJpg(request, response, nlen * 16, 24, 0, 0, 6, 0,
				getNumber(nlen), 16);
	}

	public static String createAscii2Jpg(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return createWebJpg(request, response, 100, 24, 0, 0, 6, 1,
				getAscii(6), 16);
	}

	public static String createGBK2Jpg(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return createWebJpg(request, response, 200, 24, 0, 0, 6, 2, getGBK(6),
				16);
	}

	public static String createNumber2Jpg(HttpServletRequest request,
			OutputStream out) throws Exception {
		return createJpg(request, out, 100, 24, getNumber(6));
	}

	public static String createNumber2Jpg(HttpServletRequest request,
			OutputStream out, int len) throws Exception {
		return createJpg(request, out, len * 16, 24, getNumber(len));
	}

	public static String createAscii2Jpg(HttpServletRequest request,
			OutputStream out) throws Exception {
		return createJpg(request, out, 100, 24, getAscii(6));
	}

	public static String createGBK2Jpg(HttpServletRequest request,
			OutputStream out) throws Exception {
		return createJpg(request, out, 200, 24, getGBK(6));
	}

	public static boolean createJpg(HttpServletRequest request,
			HttpServletResponse response, String imagefile, String text,
			int fontsize) throws Exception {
		return createJpg(request, response, imagefile, text, -1, -1, fontsize,
				-1, -1, -1);
	}

	public static boolean createJpg(HttpServletRequest request,
			HttpServletResponse response, String imagefile, String text,
			int fontsize, int red, int green, int blue) throws Exception {
		return createJpg(request, response, imagefile, text, -1, -1, fontsize,
				red, green, blue);
	}

	public static boolean createJpg(HttpServletRequest request,
			HttpServletResponse response, String imagefile, String text, int X,
			int Y, int fontsize) throws Exception {
		return createJpg(request, response, imagefile, text, X, Y, fontsize,
				-1, -1, -1);
	}

	public static boolean createJpg(HttpServletRequest request,
			HttpServletResponse response, String imagefile, String text, int X,
			int Y, int fontsize, int red, int green, int blue) throws Exception {
		response.setContentType("image/jpeg");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0L);
		try {
			return createJpg(request, response.getOutputStream(), imagefile,
					text, X, Y, fontsize, red, green, blue);
		} catch (Exception e) {
			throw e;
		}
	}

	public static String createJpg(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return createWebJpg(request, response, 100, 24, 0, 0, 6, 0,
				getAscii(6), 16);
	}

	public static String createJpg(HttpServletRequest request,
			HttpServletResponse response, int nlen) throws Exception {
		return createWebJpg(request, response, nlen * 16, 24, 0, 0, nlen, 0,
				getAscii(nlen), 16);
	}

	public static String createJpg(HttpServletRequest request,
			HttpServletResponse response, String drawStr) throws Exception {
		int n1 = drawStr.getBytes().length;
		int n2 = drawStr.length();
		createWebJpg(request, response, n1 * 16, 24, 0, 0, n2, 0, drawStr, 16);
		return drawStr;
	}

	public static String createJpg(HttpServletRequest request,
			HttpServletResponse response, int width, int height, String drawStr)
			throws Exception {
		int n2 = drawStr.length();
		createWebJpg(request, response, width, height, 0, 0, n2, 0, drawStr, 16);
		return drawStr;
	}

	public static String createJpg(HttpServletRequest request,
			HttpServletResponse response, int width, int height,
			String drawStr, int nfontsize) throws Exception {
		int n2 = drawStr.length();
		createWebJpg(request, response, width, height, 0, 0, n2, 0, drawStr,
				nfontsize);
		return drawStr;
	}

	public static String createJpg(HttpServletRequest request,
			HttpServletResponse response, int width, int height, int x, int y,
			int fontsize) throws Exception {
		String _s = getAscii(6);
		createWebJpg(request, response, width, height, x, y, 6, 0, _s, fontsize);
		return _s;
	}

	public static String createJpg(HttpServletRequest request,
			OutputStream out, String drawStr) throws Exception {
		int n1 = drawStr.getBytes().length;
		createJpg(request, out, n1 * 16, 24, drawStr);
		return drawStr;
	}

	public static String createJpg(HttpServletRequest request, OutputStream out)
			throws Exception {
		return createJpg(request, out, 100, 24, getAscii(6));
	}

	public static String createJpg(HttpServletRequest request,
			OutputStream out, int nlen) throws Exception {
		return createJpg(request, out, nlen * 16, 24, getAscii(nlen));
	}

	public static void createBlankJpg(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		createWebJpg(request, response, 100, 24, 0, 0, 6, 0, " ", 16);
	}

	public static boolean scale(String srcImageFile, String result, int scale,
			boolean flag) throws IOException {
		try {
			BufferedImage src = ImageIO.read(new File(srcImageFile));
			String stype = "PNG";
			if (srcImageFile.toLowerCase().endsWith(".jpg"))
				stype = "JPEG";
			else if (!result.toLowerCase().endsWith(".png"))
				result = result + ".png";
			int width = src.getWidth();
			int height = src.getHeight();
			if (flag) {
				width *= scale;
				height *= scale;
			} else {
				width /= scale;
				height /= scale;
			}
			Image image = src.getScaledInstance(width, height, 1);
			BufferedImage tag = new BufferedImage(width, height, 1);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null);
			g.dispose();
			ImageIO.write(tag, stype, new File(result));
			return true;
		} catch (IOException e) {
			throw e;
		}
	}

	public static boolean scale(String srcImageFile, String result, int width,
			int height) throws IOException {
		try {
			String stype = "PNG";
			if (srcImageFile.toLowerCase().endsWith(".jpg"))
				stype = "JPEG";
			else if (!result.toLowerCase().endsWith(".png"))
				result = result + ".png";
			BufferedImage src = ImageIO.read(new File(srcImageFile));
			Image image = src.getScaledInstance(width, height, 1);
			BufferedImage tag = new BufferedImage(width, height, 1);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null);
			g.dispose();
			ImageIO.write(tag, stype, new File(result));
			return true;
		} catch (IOException e) {
			throw e;
		}
	}

	public static boolean cut(String srcImageFile, String descDir,
			int destWidth, int destHeight) throws Exception {
		try {
			String stype = "PNG";
			if (srcImageFile.toLowerCase().endsWith(".jpg"))
				stype = "JPEG";
			else if (!srcImageFile.toLowerCase().endsWith(".png")) {
				srcImageFile = srcImageFile + ".png";
			}

			BufferedImage bi = ImageIO.read(new File(srcImageFile));
			int srcWidth = bi.getHeight();
			int srcHeight = bi.getWidth();
			if ((srcWidth > destWidth) && (srcHeight > destHeight)) {
				Image image = bi.getScaledInstance(srcWidth, srcHeight, 1);
				destWidth = 200;
				destHeight = 150;
				int cols = 0;
				int rows = 0;

				if (srcWidth % destWidth == 0)
					cols = srcWidth / destWidth;
				else {
					cols = (int) Math.floor(srcWidth / destWidth) + 1;
				}
				if (srcHeight % destHeight == 0)
					rows = srcHeight / destHeight;
				else {
					rows = (int) Math.floor(srcHeight / destHeight) + 1;
				}

				for (int i = 0; i < rows; ++i) {
					for (int j = 0; j < cols; ++j) {
						ImageFilter cropFilter = new CropImageFilter(j * 200,
								i * 150, destWidth, destHeight);
						Image img = Toolkit.getDefaultToolkit().createImage(
								new FilteredImageSource(image.getSource(),
										cropFilter));
						BufferedImage tag = new BufferedImage(destWidth,
								destHeight, 1);
						Graphics g = tag.getGraphics();
						g.drawImage(img, 0, 0, null);
						g.dispose();

						ImageIO.write(tag, stype, new File(descDir + "pre_map_"
								+ i + "_" + j + ".jpg"));
					}
				}
			}
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	public static boolean convert(String source, String result)
			throws Exception {
		try {
			String stype = "PNG";
			if (result.toLowerCase().endsWith(".jpg"))
				stype = "JPEG";
			else if (!result.toLowerCase().endsWith(".png"))
				result = result + ".png";
			File f = new File(source);
			f.canRead();
			f.canWrite();
			BufferedImage src = ImageIO.read(f);
			ImageIO.write(src, stype, new File(result));
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	public static boolean gray(String source, String result) throws IOException {
		try {
			String stype = "PNG";
			if (result.toLowerCase().endsWith(".jpg"))
				stype = "JPEG";
			else if (!result.toLowerCase().endsWith(".png"))
				result = result + ".png";
			BufferedImage src = ImageIO.read(new File(source));
			ColorSpace cs = ColorSpace.getInstance(1003);
			ColorConvertOp op = new ColorConvertOp(cs, null);
			src = op.filter(src, null);
			ImageIO.write(src, stype, new File(result));
			return true;
		} catch (IOException e) {
			throw e;
		}
	}

	public static String createJPG(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
//		response.setContentType("image/jpeg");
//		response.setHeader("Pragma", "No-cache");
//		response.setHeader("Cache-Control", "no-cache");
//		response.setDateHeader("Expires", 0L);

		BufferedImage img = new BufferedImage(50, 25, 1);

		Graphics g = img.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		Random r = new Random();
		StringBuffer sb = new StringBuffer();

		Font font = new Font("Arial", 0, 20);
		g.setFont(font);

		String str = null;

		for (int i = 0; i < 4; ++i) {
			Color color = new Color(r.nextInt(255), r.nextInt(255),
					r.nextInt(255));

			g.setColor(color);

			if (r.nextInt(2) == 0) {
				str = String.valueOf(r.nextInt(10));
			} else {
				str = String.valueOf(Character.toChars(65 + r.nextInt(26)));
			}

			g.drawString(str, 3 + 10 * i + r.nextInt(5), 15 + r.nextInt(8));

			sb.append(str);
		}

		request.getSession().setAttribute("validateCode", sb.toString());
		try {
			ServletOutputStream os = response.getOutputStream();

			ImageIO.write(img, "JPEG", os);
			os.flush();
			os.close();
			os = null;
			return sb.toString();
		} catch (IOException e) {
			throw e;
		}
	}

}
