package com.kcp.platform.bpm.graph;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kcp.platform.util.StringUtils;

/**
 * 解析流程设计器的流程图各个节点及连接线的位置
 * @author Administrator
 *
 */
public class TransformUtil {
	public static int Offset = 24;

	public static int minLen = 4;

	protected static Logger logger = LoggerFactory.getLogger(TransformUtil.class);
	
	/**
	 * 计算连接点位置
	 * @param link
	 * @return
	 */
	private static List<Point> calcLinkPoints(Link link) {
		List<Point> points = new ArrayList<Point>();
		switch (link.getShapeType().ordinal()) {
		case 0:
			points = calcStraightLinkPoints(link);
			break;
		case 1:
			points = calcFreeLinkPoints(link);
			break;
		case 3:
			points = calcOrthogonalLinkPoints(link);
			break;
		case 2:
			points = calcOrthogonalLinkPoints(link);
			break;
		default:
			points = calcOrthogonalLinkPoints(link);
		}
		return points;
	}
	
	/**
	 * 计算直角链接点
	 * @param link
	 * @return
	 */
	private static List<Point> calcOrthogonalLinkPoints(Link link) {
		List<Point> points = new ArrayList<Point>();
		Shape startNode = link.getStartNode();
		Shape endNode = link.getEndNode();
		Port startPort = link.getStartPort();
		Port endPort = link.getEndPort();
		OrthogonalType type = calcOrthogonalLinkRelativPosition(startNode, endNode, startPort, endPort);

		switch (type) {
		case BottomBottom:
			logger.info("Bottom Bottom");
			points = caculateBottomBottom(startNode, endNode, startPort, endPort);
			break;
		case BottomRight:
			logger.info("Bottom Right");
			points = caculateBottomRight(startNode, endNode, startPort, endPort);
			break;
		case BottomTop:
			logger.info("Bottom Top");
			points = caculateBottomTop(startNode, endNode, startPort, endPort);
			break;
		case BottomLeft:
			logger.info(" Bottom Left ");
			points = caculateBottomLeft(startNode, endNode, startPort, endPort);
			break;
		case RightBottom:
			logger.info(" Right Bottom ");
			points = caculateRightBottom(startNode, endNode, startPort, endPort);
			break;
		case RightRight:
			logger.info(" Right Right ");
			points = caculateRightRight(startNode, endNode, startPort, endPort);
			break;
		case RightTop:
			logger.info(" Right Top ");
			points = caculateRightTop(startNode, endNode, startPort, endPort);
			break;
		case RightLeft:
			logger.info(" Right Left ");
			points = caculateRightLeft(startNode, endNode, startPort, endPort);
			break;
		case TopBottom:
			logger.info(" Top Bottom ");
			points = caculateTopBottom(startNode, endNode, startPort, endPort);
			break;
		case TopRight:
			logger.info(" Top Right ");
			points = caculateTopRight(startNode, endNode, startPort, endPort);
			break;
		case TopTop:
			logger.info(" Top Top ");
			points = caculateTopTop(startNode, endNode, startPort, endPort);
			break;
		case TopLeft:
			logger.info(" Top Left ");
			points = caculateTopLeft(startNode, endNode, startPort, endPort);
			break;
		case LeftBottom:
			logger.info(" Left Bottom ");
			points = caculateLeftBottom(startNode, endNode, startPort, endPort);
			break;
		case LeftRight:
			logger.info(" Left Right ");
			points = caculateLeftRight(startNode, endNode, startPort, endPort);
			break;
		case LeftTop:
			logger.info(" Left Top ");
			points = caculateLeftTop(startNode, endNode, startPort, endPort);
			break;
		case LeftLeft:
			logger.info(" Left Left ");
			points = caculateLeftLeft(startNode, endNode, startPort, endPort);
		}

		return points;
	}
	
	/**
	 * 计算两个节点间连线的位置关系
	 * @param source
	 * @param target
	 * @param startPort
	 * @param endPort
	 * @return
	 */
	private static OrthogonalType calcOrthogonalLinkRelativPosition(Shape source, Shape target, Port startPort, Port endPort) {
		double startx = source.getX() + source.getW() * startPort.getX() + startPort.getHorizontalOffset();//开始连接点的绝对x值
		double starty = source.getY() + source.getH() * startPort.getY() + startPort.getVerticalOffset();//开始连接点的绝对y值
		Point start = new Point((float) startx, (float) starty);
		
		double endx = target.getX() + target.getW() * endPort.getX() + endPort.getHorizontalOffset();//结束连接点的绝对x值
		double endy = target.getY() + target.getH() * endPort.getY() + endPort.getVerticalOffset();//结束连接点的绝对y值
		Point end = new Point((float) endx, (float) endy);
		
		//计算开始节点的左上、右上、左下、右下及中间点的位置
		Point splt = new Point(source.getX(), source.getY());//左上
		Point sprt = new Point(source.getX() + source.getW(), source.getY());//右上
		Point sprb = new Point(source.getX() + source.getW(), source.getY() + source.getH());//右下
		Point splb = new Point(source.getX(), source.getY() + source.getH());//左下
		Point spc = new Point(source.getX() + source.getW() / 2.0F, source.getY() + source.getH() / 2.0F);//中间点

		//计算结束节点的左上、右上、左下、右下及中间点的位置
		Point tplt = new Point(target.getX(), target.getY());//左上
		Point tprt = new Point(target.getX() + target.getW(), target.getY());//右上
		Point tprb = new Point(target.getX() + target.getW(), target.getY() + target.getH());//右下
		Point tplb = new Point(target.getX(), target.getY() + target.getH());//左下
		Point tpc = new Point(target.getX() + target.getW() / 2.0F, target.getY() + target.getH() / 2.0F);//中间点
		
		//计算2个节点连接点关系
		if (isInsideTriangle(splt, sprt, spc, start)) {
			if (isInsideTriangle(tplt, tprt, tpc, end))
				return OrthogonalType.TopTop;//上上
			if (isInsideTriangle(tprt, tprb, tpc, end))
				return OrthogonalType.TopRight;//上右
			if (isInsideTriangle(tprb, tplb, tpc, end))
				return OrthogonalType.TopBottom;//上下
			if (isInsideTriangle(tplb, tplt, tpc, end)) {
				return OrthogonalType.TopLeft;//上左
			}
		} else if (isInsideTriangle(sprt, sprb, spc, start)) {
			if (isInsideTriangle(tplt, tprt, tpc, end))
				return OrthogonalType.RightTop;//右上
			if (isInsideTriangle(tprt, tprb, tpc, end))
				return OrthogonalType.RightRight;//右右
			if (isInsideTriangle(tprb, tplb, tpc, end))
				return OrthogonalType.RightBottom;//右下
			if (isInsideTriangle(tplb, tplt, tpc, end))
				return OrthogonalType.RightLeft;//右下
		} else if (isInsideTriangle(sprb, splb, spc, start)) {
			if (isInsideTriangle(tplt, tprt, tpc, end))
				return OrthogonalType.BottomTop;//下上
			if (isInsideTriangle(tprt, tprb, tpc, end))
				return OrthogonalType.BottomRight;//下右
			if (isInsideTriangle(tprb, tplb, tpc, end))
				return OrthogonalType.BottomBottom;//下下
			if (isInsideTriangle(tplb, tplt, tpc, end))
				return OrthogonalType.BottomLeft;//下左
		} else if (isInsideTriangle(splb, splt, spc, start)) {
			if (isInsideTriangle(tplt, tprt, tpc, end))
				return OrthogonalType.LeftTop;//左上
			if (isInsideTriangle(tprt, tprb, tpc, end))
				return OrthogonalType.LeftRight;//左右
			if (isInsideTriangle(tprb, tplb, tpc, end))
				return OrthogonalType.LeftBottom;//左下
			if (isInsideTriangle(tplb, tplt, tpc, end)) {
				return OrthogonalType.LeftLeft;//左左
			}
		}
		return OrthogonalType.RightLeft;
	}
	
	/**
	 * 计算链接点p是否在A,B,C 3个连接点范围内
	 * @param A
	 * @param B
	 * @param C
	 * @param P
	 * @return
	 */
	private static boolean isInsideTriangle(Point A, Point B, Point C, Point P) {
		double abc = triangleArea(A, B, C);
        double abp = triangleArea(A, B, P);
        double acp = triangleArea(A, C, P);
        double bcp = triangleArea(B, C, P);
        if (abc == abp + acp + bcp) {
            return true;
        } else {
            return false;
        }
	}
	/**
	 * 返回三个点组成三角形的面积
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	private static double triangleArea(Point a, Point b, Point c) {
		double result = Math.abs((a.getX() * b.getY() + b.getX() * c.getY() + c.getX() * a.getY() - b.getX() * a.getY()
				- c.getX() * b.getY() - a.getX() * c.getY()) / 2.0D);
		return result;
	}
	/**
	 * 获取直线连接点
	 * @param link
	 * @return
	 */
	private static List<Point> calcStraightLinkPoints(Link link) {
		link.getIntermediatePoints().clear();
		return calcFreeLinkPoints(link);
	}
	
	/**
	 * 获取自由连线的连接点
	 * @param link
	 * @return
	 */
	private static List<Point> calcFreeLinkPoints(Link link) {
		List<Point> points = new ArrayList<Point>();
		Shape startNode = link.getStartNode();
		Shape endNode = link.getEndNode();
		Port startPort = link.getStartPort();
		Port endPort = link.getEndPort();
		List<Point> intermediatePoints = link.getIntermediatePoints();

		double firstX = startNode.getX() + startNode.getW() * startPort.getX()
				+ startPort.getHorizontalOffset();
		double firstY = startNode.getY() + startNode.getH() * startPort.getY()
				+ startPort.getVerticalOffset();
		Point first = new Point((float) firstX, (float) firstY);
		double lastX = endNode.getX() + endNode.getW() * endPort.getX()
				+ endPort.getHorizontalOffset();
		double lastY = endNode.getY() + endNode.getH() * endPort.getY()
				+ endPort.getVerticalOffset();
		Point last = new Point((float) lastX, (float) lastY);

		Point temp1 = intermediatePoints.size() > 0 ? (Point) intermediatePoints
				.get(0) : last;
		Point temp2 = intermediatePoints.size() > 0 ? (Point) intermediatePoints
				.get(intermediatePoints.size() - 1) : first;
		Point temp3 = null;
		Point temp4 = null;
		temp3 = handleOverlap(startNode, first, temp1);
		temp4 = handleOverlap(endNode, last, temp2);
		points.add(temp3 == null ? first : temp3);
		points.addAll(intermediatePoints);
		points.add(temp4 == null ? last : temp4);

		return points;
	}

	private static Point handleOverlap(Shape shape, Point p1, Point p2) {
		Point a = new Point(shape.getX(), shape.getY());
		Point b = new Point(shape.getX() + shape.getW(), shape.getY());
		Point c = new Point(shape.getX() + shape.getW(), shape.getY()
				+ shape.getH());
		Point d = new Point(shape.getX(), shape.getY() + shape.getH());

		Point[] cps = new Point[4];

		if (!isInsideLine(a, b, p1)) {
			cps[0] = getCrosspoint(p1, p2, a, b);
		}
		if (!isInsideLine(b, c, p1)) {
			cps[1] = getCrosspoint(p1, p2, b, c);
		}
		if (!isInsideLine(c, d, p1)) {
			cps[2] = getCrosspoint(p1, p2, c, d);
		}
		if (!isInsideLine(d, a, p1)) {
			cps[3] = getCrosspoint(p1, p2, d, a);
		}
		for (Point p : cps) {
			if (p != null) {
				return p;
			}

		}

		return null;
	}

	private static boolean isInsideLine(Point p1, Point p2, Point p) {
		double x = p.getX();
		double y = p.getY();
		double x1 = p1.getX();
		double y1 = p1.getY();
		double x2 = p2.getX();
		double y2 = p2.getY();
		if (Double.compare(x1, x2) == 0) {
			if (Double.compare(x, x2) == 0) {
				return true;
			}
			return false;
		}

		if (Double.compare(y1, y2) == 0) {
			if (Double.compare(y, y2) == 0) {
				return true;
			}
			return false;
		}

		double s1 = x - x1;
		double t1 = y - y1;
		double s2 = x1 - x2;
		double t2 = y1 - y2;
		return Double.compare(s1 / s2, t1 / t2) == 0;
	}

	private static Point getCrosspoint(Point a, Point b, Point c, Point d) {
		float ax = a.getX();
		float ay = a.getY();
		float bx = b.getX();
		float by = b.getY();
		float cx = c.getX();
		float cy = c.getY();
		float dx = d.getX();
		float dy = d.getY();

		if (Math.abs(by - ay) + Math.abs(bx - ax) + Math.abs(dy - cy)
				+ Math.abs(dx - cx) == 0.0F) {
			if (cx - ax + (cy - ay) == 0.0F) {
				return new Point(ax, ay);
			}

			return null;
		}
		if (Math.abs(by - ay) + Math.abs(bx - ax) == 0.0F) {
			if ((ax - dx) * (cy - dy) - (ay - dy) * (cx - dx) == 0.0F) {
				return new Point(ax, ay);
			}

			return null;
		}
		if (Math.abs(dy - cy) + Math.abs(dx - cx) == 0.0F) {
			if ((dx - bx) * (ay - by) - (dy - by) * (ax - bx) == 0.0F) {
				return new Point(cx, cy);
			}

			return null;
		}
		if ((by - ay) * (cx - dx) - (bx - ax) * (cy - dy) == 0.0F) {
			return null;
		}

		float x = ((bx - ax) * (cx - dx) * (cy - ay) - cx * (bx - ax)
				* (cy - dy) + ax * (by - ay) * (cx - dx))
				/ ((by - ay) * (cx - dx) - (bx - ax) * (cy - dy));
		float y = ((by - ay) * (cy - dy) * (cx - ax) - cy * (by - ay)
				* (cx - dx) + ay * (bx - ax) * (cy - dy))
				/ ((bx - ax) * (cy - dy) - (by - ay) * (cx - dx));

		if (((x - ax) * (x - bx) <= 0.0F) && ((x - cx) * (x - dx) <= 0.0F)
				&& ((y - ay) * (y - by) <= 0.0F)
				&& ((y - cy) * (y - dy) <= 0.0F)) {
			return new Point(x, y);
		}

		return null;
	}

	public static String add(String para1, String para2) {
		double d1 = 0.0D;
		double d2 = 0.0D;
		if (StringUtils.isNotEmpty(para1)) {
			d1 = Double.parseDouble(para1);
		}
		if (StringUtils.isNotEmpty(para2)) {
			d2 = Double.parseDouble(para2);
		}
		d1 += d2;

		return String.valueOf(d1);
	}

	public static String bold(String str) {
		return "<b>" + str + "</b>";
	}

	public static double nan2Zero(String str) {
		if ((StringUtils.isEmpty(str)) || (str.equalsIgnoreCase("NaN"))) {
			return 0.0D;
		}
		return Double.parseDouble(str);
	}

	public static void display(String str) {
		logger.info(str);
	}

	public static int splitLength(String str, String regex) {
		int len = str.split(regex).length;
		return len;
	}

	public static String accumulate(String childrenY, String parentY) {
		double retVal = 0.0D;
		if (!StringUtils.isEmpty(childrenY)) {
			String[] childrenY_ = childrenY.split(",");
			for (String y : childrenY_) {
				retVal += nan2Zero(y);
			}
		}
		if (StringUtils.isNotEmpty(parentY)) {
			retVal += nan2Zero(parentY);
		}
		return String.valueOf(retVal);
	}

	public static String min(String para1, String para2) {
		double d1 = 0.0D;
		double d2 = 0.0D;
		if (StringUtils.isNotEmpty(para1)) {
			d1 = Double.parseDouble(para1);
		}
		if (StringUtils.isNotEmpty(para2)) {
			d2 = Double.parseDouble(para2);
		}
		if (d1 < d2) {
			return String.valueOf(d1);
		}
		return String.valueOf(d2);
	}
	/**
	 * 计算连接点串
	 * @param shapeType
	 * @param fName
	 * @param fX
	 * @param fY
	 * @param fW
	 * @param fH
	 * @param sPortType
	 * @param sPortX
	 * @param sPortY
	 * @param sPortHOffset
	 * @param sPortVOffset
	 * @param tName
	 * @param tX
	 * @param tY
	 * @param tW
	 * @param tH
	 * @param tPortType
	 * @param tPortX
	 * @param tPortY
	 * @param tPortHOffset
	 * @param tPortVOffset
	 * @param points
	 * @return
	 */
	public static String calc(String shapeType, String fName, String fX,
			String fY, String fW, String fH, String sPortType, String sPortX,
			String sPortY, String sPortHOffset, String sPortVOffset,
			String tName, String tX, String tY, String tW, String tH,
			String tPortType, String tPortX, String tPortY,
			String tPortHOffset, String tPortVOffset, String points) {
		sPortX = ("".equals(sPortX)) || (sPortX == null) ? "0.5" : sPortX;
		sPortY = ("".equals(sPortY)) || (sPortY == null) ? "0.5" : sPortY;
		tPortX = ("".equals(tPortX)) || (tPortX == null) ? "0.5" : tPortX;
		tPortY = ("".equals(tPortY)) || (tPortY == null) ? "0.5" : tPortY;

		Link link = new Link();

		if (ShapeType.FREE.getText().equalsIgnoreCase(shapeType))
			link.setShapeType(ShapeType.FREE);
		else if (ShapeType.STRAIGHT.getText().equalsIgnoreCase(shapeType))
			link.setShapeType(ShapeType.STRAIGHT);
		else if (ShapeType.OBLIQUE.getText().equalsIgnoreCase(shapeType))
			link.setShapeType(ShapeType.OBLIQUE);
		else {
			link.setShapeType(ShapeType.ORTHOGONAL);
		}

		float startX = (float) nan2Zero(fX);
		float startY = (float) nan2Zero(fY);
		float startW = (float) nan2Zero(fW);
		float startH = (float) nan2Zero(fH);
		Shape startShape = new Shape(fName, startX, startY, startW, startH);
		float endX = (float) nan2Zero(tX);
		float endY = (float) nan2Zero(tY);
		float endW = (float) nan2Zero(tW);
		float endH = (float) nan2Zero(tH);
		Shape endShape = new Shape(tName, endX, endY, endW, endH);
		PortType startPortType;
		if (PortType.NODE_PART_REFERENCE.getText().equalsIgnoreCase(sPortType)) {
			startPortType = PortType.NODE_PART_REFERENCE;
		} else {
			if (PortType.AUTOMATIC_SIDE.getText().equalsIgnoreCase(sPortType))
				startPortType = PortType.AUTOMATIC_SIDE;
			else
				startPortType = PortType.POSITION;
		}
		float startPortX = (float) nan2Zero(sPortX);
		float startPortY = (float) nan2Zero(sPortY);
		float startPortVOffset = (float) nan2Zero(sPortVOffset);
		float startPortHOffset = (float) nan2Zero(sPortHOffset);
		Port startPort = new Port(startPortType, startPortX, startPortY,
				startPortHOffset, startPortVOffset, null, false);
		PortType endPortType;
		if (PortType.NODE_PART_REFERENCE.getText().equalsIgnoreCase(tPortType)) {
			endPortType = PortType.NODE_PART_REFERENCE;
		} else {
			if (PortType.AUTOMATIC_SIDE.getText().equalsIgnoreCase(tPortType))
				endPortType = PortType.AUTOMATIC_SIDE;
			else
				endPortType = PortType.POSITION;
		}
		float endPortX = (float) nan2Zero(tPortX);
		float endPortY = (float) nan2Zero(tPortY);
		float endPortVOffset = (float) nan2Zero(tPortVOffset);
		float endPortHOffset = (float) nan2Zero(tPortHOffset);
		Port endPort = new Port(endPortType, endPortX, endPortY,
				endPortHOffset, endPortVOffset, null, false);

		List<Point> intermediatePoints = new ArrayList<Point>();
		if ((points != null) && (!"".equals(points))) {
			String[] pointAry = points.split(",");
			for (String p : pointAry) {
				String[] point = p.split(":");
				double x = nan2Zero(point[0]);
				double y = nan2Zero(point[1]);
				intermediatePoints.add(new Point((float) x, (float) y));
			}
		}

		link.setStartNode(startShape);
		link.setEndNode(endShape);
		link.setStartPort(startPort);
		link.setEndPort(endPort);
		link.setIntermediatePoints(intermediatePoints);
		List<Point> linkPoints = calcLinkPoints(link);
		return getPointXml(linkPoints);
	}
	/**
	 * 计算连接点
	 * @param fromShape
	 * @param toShape
	 * @param fromPort
	 * @param toPort
	 * @return
	 */
	public static List<Point> caculatePoints(Shape fromShape, Shape toShape,
			Port fromPort, Port toPort) {
		List<Point> list = null;
		switch (fromShape.getDirectory().ordinal()) {
		case 0:
			switch (toShape.getDirectory().ordinal()) {
			case 0:
				logger.info("Top Top");
				list = caculateTopTop(fromShape, toShape, fromPort, toPort);
				break;
			case 2:
				logger.info("Top Left");
				list = caculateTopLeft(fromShape, toShape, fromPort, toPort);
				break;
			case 3:
				logger.info("Top Right");
				list = caculateTopRight(fromShape, toShape, fromPort, toPort);
				break;
			case 1:
				logger.info("Top Bottom");
				list = caculateTopBottom(fromShape, toShape, fromPort, toPort);
			}

			break;
		case 2:
			switch (toShape.getDirectory().ordinal()) {
			case 0:
				logger.info(" Left Top ");
				list = caculateLeftTop(fromShape, toShape, fromPort, toPort);
				break;
			case 2:
				logger.info(" Left Left ");
				list = caculateLeftLeft(fromShape, toShape, fromPort, toPort);
				break;
			case 3:
				logger.info(" Left Right ");
				list = caculateLeftRight(fromShape, toShape, fromPort, toPort);
				break;
			case 1:
				logger.info(" Left Bottom ");
				list = caculateLeftBottom(fromShape, toShape, fromPort, toPort);
			}

			break;
		case 3:
			switch (toShape.getDirectory().ordinal()) {
			case 0:
				logger.info(" Right Top ");
				list = caculateRightTop(fromShape, toShape, fromPort, toPort);
				break;
			case 2:
				logger.info(" Right Left ");
				list = caculateRightLeft(fromShape, toShape, fromPort, toPort);
				break;
			case 3:
				logger.info(" Right Right ");
				list = caculateRightRight(fromShape, toShape, fromPort, toPort);
				break;
			case 1:
				logger.info(" Right Bottom ");
				list = caculateRightBottom(fromShape, toShape, fromPort, toPort);
			}

			break;
		case 1:
			switch (toShape.getDirectory().ordinal()) {
			case 0:
				logger.info(" Bottom Top ");
				list = caculateBottomTop(fromShape, toShape, fromPort, toPort);
				break;
			case 2:
				logger.info(" Bottom Left ");
				list = caculateBottomLeft(fromShape, toShape, fromPort, toPort);
				break;
			case 3:
				logger.info(" Bottom Right ");
				list = caculateBottomRight(fromShape, toShape, fromPort, toPort);
				break;
			case 1:
				logger.info(" Bottom Bottom ");
				list = caculateBottomBottom(fromShape, toShape, fromPort,
						toPort);
			}
			break;
		}

		return list;
	}

	public static String caculate(Shape fromShape, Shape toShape) {
		return null;
	}
	/**
	 * 计算起始连接点在头部，最终连接点在头部的连线坐标
	 * @param fromShape
	 * @param toShape
	 * @param fromPort
	 * @param toPort
	 * @return
	 */
	public static List<Point> caculateTopTop(Shape fromShape, Shape toShape, Port fromPort, Port toPort) {
		List<Point> list = new ArrayList<Point>();
		float fromX = (float) (fromShape.getX() + fromShape.getW()
				* fromPort.getX() + fromPort.getHorizontalOffset());
		float fromY = (float) (fromShape.getY() + fromShape.getH()
				* fromPort.getY() + fromPort.getVerticalOffset());
		float toX = (float) (toShape.getX() + toShape.getW() * toPort.getX() + toPort
				.getHorizontalOffset());
		float toY = (float) (toShape.getY() + toShape.getH() * toPort.getY() + toPort
				.getVerticalOffset());

		if (toShape.getBottomRightY() + minLen < fromShape.getY() - minLen) {
			if ((toShape.getX() - minLen > fromX)
					|| (toShape.getBottomRightX() + minLen < fromX)) {
				float tmpy = toShape.getY() - Offset;

				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(fromX, tmpy);
				Point p3 = new Point(toX, tmpy);
				Point p4 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
			} else {
				float tmpx = 0.0F;
				float tmpy = (fromShape.getY() + toShape.getBottomRightY()) / 2.0F;
				if (toX <= fromX)
					tmpx = toShape.getBottomRightX() + Offset;
				else {
					tmpx = toShape.getX() - Offset;
				}
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(fromX, tmpy);
				Point p3 = new Point(tmpx, tmpy);
				Point p4 = new Point(tmpx, toY - Offset);
				Point p5 = new Point(toX, toY - Offset);
				Point p6 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
				list.add(p5);
				list.add(p6);
			}
		} else if (toShape.getY() - minLen < fromShape.getBottomRightY()
				+ minLen) {
			float tmpy = toShape.getY() - Offset;
			if (toShape.getY() < fromShape.getY())
				tmpy = toShape.getY() - Offset;
			else {
				tmpy = fromShape.getY() - Offset;
			}
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(fromX, tmpy);
			Point p3 = new Point(toX, tmpy);
			Point p4 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
		} else if ((fromShape.getX() - minLen > toX)
				|| (fromShape.getBottomRightX() + minLen < toX)) {
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(fromX, fromY - Offset);
			Point p3 = new Point(toX, fromY - Offset);
			Point p4 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
		} else {
			float tmpy = (fromShape.getBottomRightY() + toShape.getY()) / 2.0F;
			float tmpx = 0.0F;
			if (fromX <= toX)
				tmpx = fromShape.getBottomRightX() + Offset;
			else {
				tmpx = fromShape.getX() - Offset;
			}
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(fromX, fromY - Offset);
			Point p3 = new Point(tmpx, fromY - Offset);
			Point p4 = new Point(tmpx, tmpy);
			Point p5 = new Point(toX, tmpy);
			Point p6 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
			list.add(p5);
			list.add(p6);
		}

		return list;
	}
	
	/**
	 * 计算起始连接点在头部，最终连接点在右边的连线坐标
	 * @param fromShape
	 * @param toShape
	 * @param fromPort
	 * @param toPort
	 * @return
	 */
	public static List<Point> caculateTopRight(Shape fromShape, Shape toShape, Port fromPort, Port toPort) {
		List<Point> list = new ArrayList<Point>();
		float fromX = (float) (fromShape.getX() + fromShape.getW() * fromPort.getX() + fromPort.getHorizontalOffset());
		float fromY = (float) (fromShape.getY() + fromShape.getH() * fromPort.getY() + fromPort.getVerticalOffset());
		float toX = (float) (toShape.getX() + toShape.getW() * toPort.getX() + toPort.getHorizontalOffset());
		float toY = (float) (toShape.getY() + toShape.getH() * toPort.getY() + toPort.getVerticalOffset());
		if (fromX >= toX - minLen) {
			if (toY < fromY - minLen) {
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(fromX, toY);
				Point p3 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
			} else if (toX + minLen < fromShape.getX() - minLen) {
				float tmpx = (fromShape.getX() + toX) / 2.0F;
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(fromX, fromY - Offset);
				Point p3 = new Point(tmpx, fromY - Offset);
				Point p4 = new Point(tmpx, toY);
				Point p5 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
				list.add(p5);
			} else {
				float tmpy = 0.0F;
				if (toShape.getY() < fromY)
					tmpy = toShape.getY() - Offset;
				else {
					tmpy = fromY - Offset;
				}
				float tmpx = fromShape.getBottomRightX() + Offset;

				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(fromX, tmpy);
				Point p3 = new Point(tmpx, tmpy);
				Point p4 = new Point(tmpx, toY);
				Point p5 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
				list.add(p5);
			}

		} else if (toShape.getBottomRightY() + minLen < fromY - minLen) {
			float tmpy = (fromY + toShape.getBottomRightY()) / 2.0F;

			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(fromX, tmpy);
			Point p3 = new Point(toX + Offset, tmpy);
			Point p4 = new Point(toX + Offset, toY);
			Point p5 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
			list.add(p5);
		} else {
			float tmpy = 0.0F;
			float tmpx = 0.0F;
			Point p1 = new Point(fromX, fromY);

			if (toX + Offset >= fromShape.getBottomRightX() + Offset)
				tmpx = toX + Offset;
			else {
				tmpx = fromShape.getBottomRightX() + Offset;
			}

			if (toShape.getY() - Offset <= fromY - Offset)
				tmpy = toShape.getY() - Offset;
			else {
				tmpy = fromY - Offset;
			}
			Point p2 = new Point(fromX, tmpy);
			Point p3 = new Point(tmpx, tmpy);
			Point p4 = new Point(tmpx, toY);
			Point p5 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
			list.add(p5);
		}

		return list;
	}
	
	/**
	 * 计算起始连接点在头部，最终连接点在底部的连线坐标
	 * @param fromShape
	 * @param toShape
	 * @param fromPort
	 * @param toPort
	 * @return
	 */
	public static List<Point> caculateTopBottom(Shape fromShape,
			Shape toShape, Port fromPort, Port toPort) {
		List<Point> list = new ArrayList<Point>();

		float fromX = (float) (fromShape.getX() + fromShape.getW()
				* fromPort.getX() + fromPort.getHorizontalOffset());
		float fromY = (float) (fromShape.getY() + fromShape.getH()
				* fromPort.getY() + fromPort.getVerticalOffset());
		float toX = (float) (toShape.getX() + toShape.getW() * toPort.getX() + toPort
				.getHorizontalOffset());
		float toY = (float) (toShape.getY() + toShape.getH() * toPort.getY() + toPort
				.getVerticalOffset());

		if (fromX < toX) {
			if (toY + minLen <= fromY - minLen) {
				float tmpy = (fromY + toY) / 2.0F;
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(fromX, tmpy);
				Point p3 = new Point(toX, tmpy);
				Point p4 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
			} else if (fromShape.getBottomRightX() + minLen < toShape.getX()
					- minLen) {
				float tmpx = (toShape.getX() + fromShape.getBottomRightX()) / 2.0F;
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(fromX, fromY - Offset);
				Point p3 = new Point(tmpx, fromY - Offset);
				Point p4 = new Point(tmpx, toY + Offset);
				Point p5 = new Point(toX, toY + Offset);
				Point p6 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
				list.add(p5);
				list.add(p6);
			} else {
				float tmpy;
				if (fromShape.getY() <= toShape.getY()) {
					tmpy = fromShape.getY() - Offset;
				} else {
					tmpy = toShape.getY() - Offset;
				}
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(fromX, tmpy);
				Point p3 = new Point(toShape.getBottomRightX() + Offset, tmpy);
				Point p4 = new Point(toShape.getBottomRightX() + Offset, toY
						+ Offset);
				Point p5 = new Point(toX, toY + Offset);
				Point p6 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
				list.add(p5);
				list.add(p6);
			}
		} else if (fromX == toX) {
			if (toY + minLen < fromY - minLen) {
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(toX, toY);
				list.add(p1);
				list.add(p2);
			} else {
				float tmpy;
				if (fromShape.getY() < toShape.getY())
					tmpy = fromShape.getY() - Offset;
				else
					tmpy = toShape.getY() - Offset;
				float tmpx;
				if (fromShape.getX() < toShape.getX())
					tmpx = fromShape.getX() - Offset;
				else {
					tmpx = toShape.getX() - Offset;
				}

				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(fromX, tmpy);
				Point p3 = new Point(tmpx, tmpy);
				Point p4 = new Point(tmpx, toY + Offset);
				Point p5 = new Point(toX, toY + Offset);
				Point p6 = new Point(toX, toY);
				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
				list.add(p5);
				list.add(p6);
			}

		} else if (toY + minLen < fromY - minLen) {
			float tmpy = (fromY + toY) / 2.0F;
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(fromX, tmpy);
			Point p3 = new Point(toX, tmpy);
			Point p4 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
		} else if (toShape.getBottomRightX() + minLen < fromShape.getX()
				- minLen) {
			float tmpx = (fromShape.getX() + toShape.getBottomRightX()) / 2.0F;
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(fromX, fromY - Offset);
			Point p3 = new Point(tmpx, fromY - Offset);
			Point p4 = new Point(tmpx, toY + Offset);
			Point p5 = new Point(toX, toY + Offset);
			Point p6 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
			list.add(p5);
			list.add(p6);
		} else {
			float tmpy;
			if (fromShape.getY() < toShape.getY())
				tmpy = fromShape.getY() - Offset;
			else
				tmpy = toShape.getY() - Offset;
			float tmpx;
			if (fromShape.getX() < toShape.getX())
				tmpx = fromShape.getX() - Offset;
			else {
				tmpx = toShape.getX() - Offset;
			}

			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(fromX, tmpy);
			Point p3 = new Point(tmpx, tmpy);
			Point p4 = new Point(tmpx, toY + Offset);
			Point p5 = new Point(toX, toY + Offset);
			Point p6 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
			list.add(p5);
			list.add(p6);
		}

		return list;
	}
	
	/**
	 * 计算起始连接点在头部，最终连接点在左边的连线坐标
	 * @param fromShape
	 * @param toShape
	 * @param fromPort
	 * @param toPort
	 * @return
	 */
	public static List<Point> caculateTopLeft(Shape fromShape,
			Shape toShape, Port fromPort, Port toPort) {
		List<Point> list = new ArrayList<Point>();

		float fromX = (float) (fromShape.getX() + fromShape.getW()
				* fromPort.getX() + fromPort.getHorizontalOffset());
		float fromY = (float) (fromShape.getY() + fromShape.getH()
				* fromPort.getY() + fromPort.getVerticalOffset());
		float toX = (float) (toShape.getX() + toShape.getW() * toPort.getX() + toPort
				.getHorizontalOffset());
		float toY = (float) (toShape.getY() + toShape.getH() * toPort.getY() + toPort
				.getVerticalOffset());

		if (toShape.getX() - minLen > fromX) {
			if (toY < fromShape.getY() - minLen) {
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(fromX, toY);
				Point p3 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
			} else if (fromShape.getBottomRightX() + minLen < toShape.getX()
					- minLen) {
				float tmpx = (toShape.getX() + fromShape.getBottomRightX()) / 2.0F;
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(fromX, fromY - Offset);
				Point p3 = new Point(tmpx, fromY - Offset);
				Point p4 = new Point(tmpx, toY);
				Point p5 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
				list.add(p5);
			} else {
				float tmpy = 0.0F;
				float tmpx = 0.0F;
				if (toShape.getY() <= fromShape.getY())
					tmpy = toShape.getY() - Offset;
				else {
					tmpy = fromShape.getY() - Offset;
				}

				if (toShape.getX() <= fromShape.getX())
					tmpx = toShape.getX() - Offset;
				else {
					tmpx = fromShape.getX() - Offset;
				}

				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(fromX, tmpy);
				Point p3 = new Point(tmpx, tmpy);
				Point p4 = new Point(tmpx, toY);
				Point p5 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
				list.add(p5);
			}

		} else if (fromShape.getY() - minLen >= toShape.getBottomRightY()
				+ minLen) {
			float tmpy = (fromShape.getY() + toShape.getBottomRightY()) / 2.0F;
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(fromX, tmpy);
			Point p3 = new Point(toX - Offset, tmpy);
			Point p4 = new Point(toX - Offset, toY);
			Point p5 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
			list.add(p5);
		} else {
			float tmpy = 0.0F;
			float tmpx = 0.0F;

			if (toShape.getY() < fromShape.getY())
				tmpy = toShape.getY() - Offset;
			else {
				tmpy = fromShape.getY() - Offset;
			}
			if (toShape.getX() < fromShape.getX())
				tmpx = toShape.getX() - Offset;
			else {
				tmpx = fromShape.getX() - Offset;
			}
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(fromX, tmpy);
			Point p3 = new Point(tmpx, tmpy);
			Point p4 = new Point(tmpx, toY);
			Point p5 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
			list.add(p5);
		}

		return list;
	}
	
	/**
	 * 计算起始连接点在右边，最终连接点在头部的连线坐标
	 * @param fromShape
	 * @param toShape
	 * @param fromPort
	 * @param toPort
	 * @return
	 */
	public static List<Point> caculateRightTop(Shape fromShape,
			Shape toShape, Port fromPort, Port toPort) {
		List<Point> list = new ArrayList<Point>();

		float fromX = (float) (fromShape.getX() + fromShape.getW()
				* fromPort.getX() + fromPort.getHorizontalOffset());
		float fromY = (float) (fromShape.getY() + fromShape.getH()
				* fromPort.getY() + fromPort.getVerticalOffset());
		float toX = (float) (toShape.getX() + toShape.getW() * toPort.getX() + toPort
				.getHorizontalOffset());
		float toY = (float) (toShape.getY() + toShape.getH() * toPort.getY() + toPort
				.getVerticalOffset());

		if (fromShape.getBottomRightX() + minLen < toShape.getX() - minLen) {
			if (fromY > toShape.getY() + minLen) {
				float tmpx = (toShape.getX() + fromX) / 2.0F;
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(tmpx, fromY);
				Point p3 = new Point(tmpx, toY - Offset);
				Point p4 = new Point(toX, toY - Offset);
				Point p5 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
				list.add(p5);
			} else {
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(toX, fromY);
				Point p3 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
			}
		} else if (toShape.getY() - minLen < fromY) {
			float tmpy = 0.0F;
			float tmpx = 0.0F;

			if (toShape.getY() < fromShape.getY())
				tmpy = toShape.getY() - Offset;
			else {
				tmpy = fromShape.getY() - Offset;
			}

			if (toShape.getBottomRightX() >= fromShape.getBottomRightX())
				tmpx = toShape.getBottomRightX() + Offset;
			else {
				tmpx = fromShape.getBottomRightX() + Offset;
			}

			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(tmpx, fromY);
			Point p3 = new Point(tmpx, tmpy);
			Point p4 = new Point(toX, tmpy);
			Point p5 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
			list.add(p5);
		} else if (toShape.getY() - minLen < fromShape.getBottomRightY()
				+ minLen) {
			if (toX > fromShape.getBottomRightX() + minLen) {
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(toX, fromY);
				Point p3 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
			} else {
				float tmpy = 0.0F;
				float tmpx = 0.0F;

				if (toShape.getY() < fromShape.getY())
					tmpy = toShape.getY() - Offset;
				else {
					tmpy = fromShape.getY() - Offset;
				}
				if (toShape.getBottomRightX() >= fromShape.getBottomRightX())
					tmpx = toShape.getBottomRightX() + Offset;
				else {
					tmpx = fromShape.getBottomRightX() + Offset;
				}

				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(tmpx, fromY);
				Point p3 = new Point(tmpx, tmpy);
				Point p4 = new Point(toX, tmpy);
				Point p5 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
				list.add(p5);
			}
		} else if (toX > fromShape.getBottomRightX() + minLen) {
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(toX, fromY);
			Point p3 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
		} else {
			float tmpy = (fromShape.getBottomRightY() + toShape.getY()) / 2.0F;
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(fromX + Offset, fromY);
			Point p3 = new Point(fromX + Offset, tmpy);
			Point p4 = new Point(toX, tmpy);
			Point p5 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
			list.add(p5);
		}

		return list;
	}
	/**
	 * 计算起始连接点在右边，最终连接点在右边的连线坐标
	 * @param fromShape
	 * @param toShape
	 * @param fromPort
	 * @param toPort
	 * @return
	 */
	public static List<Point> caculateRightRight(Shape fromShape,
			Shape toShape, Port fromPort, Port toPort) {
		List<Point> list = new ArrayList<Point>();

		float fromX = (float) (fromShape.getX() + fromShape.getW()
				* fromPort.getX() + fromPort.getHorizontalOffset());
		float fromY = (float) (fromShape.getY() + fromShape.getH()
				* fromPort.getY() + fromPort.getVerticalOffset());
		float toX = (float) (toShape.getX() + toShape.getW() * toPort.getX() + toPort
				.getHorizontalOffset());
		float toY = (float) (toShape.getY() + toShape.getH() * toPort.getY() + toPort
				.getVerticalOffset());

		if (toShape.getX() - minLen >= fromShape.getBottomRightX() + minLen) {
			if ((toShape.getBottomRightY() + minLen < fromY)
					|| (toShape.getY() - minLen > fromY)) {
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(toX + Offset, fromY);
				Point p3 = new Point(toX + Offset, toY);
				Point p4 = new Point(toX, toY);
				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
			} else {
				float tmpx = (toShape.getX() + fromX) / 2.0F;
				float tmpy = 0.0F;
				if (fromY > toY)
					tmpy = toShape.getBottomRightY() + Offset;
				else {
					tmpy = toShape.getY() - Offset;
				}
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(tmpx, fromY);
				Point p3 = new Point(tmpx, tmpy);
				Point p4 = new Point(toX + Offset, tmpy);
				Point p5 = new Point(toX + Offset, toY);
				Point p6 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
				list.add(p5);
				list.add(p6);
			}

		} else if (toShape.getBottomRightX() + minLen > fromShape.getX()
				- minLen) {
			float tmpx = 0.0F;
			if (toShape.getBottomRightX() > fromShape.getBottomRightX())
				tmpx = toShape.getBottomRightX() + Offset;
			else {
				tmpx = fromShape.getBottomRightX() + Offset;
			}

			if (fromY == toY) {
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(tmpx, fromY);
				Point p3 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
			} else {
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(tmpx, fromY);
				Point p3 = new Point(tmpx, toY);
				Point p4 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
			}

		} else if ((toShape.getBottomRightY() + minLen <= fromShape.getY()
				- minLen)
				|| (toShape.getY() - minLen > fromShape.getBottomRightY()
						+ minLen)) {
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(fromX + Offset, fromY);
			Point p3 = new Point(fromX + Offset, toY);
			Point p4 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
		} else {
			float tmpx = (fromShape.getX() + toShape.getBottomRightX()) / 2.0F;
			float tmpy = 0.0F;
			if (fromY <= toY)
				tmpy = fromShape.getBottomRightY() + Offset;
			else {
				tmpy = fromShape.getY() - Offset;
			}

			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(fromX + Offset, fromY);
			Point p3 = new Point(fromX + Offset, tmpy);
			Point p4 = new Point(tmpx, tmpy);
			Point p5 = new Point(tmpx, toY);
			Point p6 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
			list.add(p5);
			list.add(p6);
		}

		return list;
	}
	
	/**
	 * 计算起始连接点在右边，最终连接点在底部的连线坐标
	 * @param fromShape
	 * @param toShape
	 * @param fromPort
	 * @param toPort
	 * @return
	 */
	public static List<Point> caculateRightBottom(Shape fromShape,
			Shape toShape, Port fromPort, Port toPort) {
		List<Point> list = new ArrayList<Point>();

		float fromX = (float) (fromShape.getX() + fromShape.getW()
				* fromPort.getX() + fromPort.getHorizontalOffset());
		float fromY = (float) (fromShape.getY() + fromShape.getH()
				* fromPort.getY() + fromPort.getVerticalOffset());
		float toX = (float) (toShape.getX() + toShape.getW() * toPort.getX() + toPort
				.getHorizontalOffset());
		float toY = (float) (toShape.getY() + toShape.getH() * toPort.getY() + toPort
				.getVerticalOffset());

		if (toX > fromShape.getBottomRightX() + minLen) {
			if (fromY > toY + minLen) {
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(toX, fromY);
				Point p3 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
			} else {
				float tmpx = (fromShape.getBottomRightX() + toShape.getX()) / 2.0F;
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(tmpx, fromY);
				Point p3 = new Point(tmpx, toY + Offset);
				Point p4 = new Point(toX, toY + Offset);
				Point p5 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
				list.add(p5);
			}

		} else if (toShape.getY() + minLen < fromShape.getY() - minLen) {
			float tmpy = (fromShape.getY() + toShape.getBottomRightY()) / 2.0F;
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(fromX + Offset, fromY);
			Point p3 = new Point(fromX + Offset, tmpy);
			Point p4 = new Point(toX, tmpy);
			Point p5 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
			list.add(p5);
		} else {
			float tmpx = 0.0F;
			float tmpy = 0.0F;
			if (toShape.getBottomRightX() > fromShape.getBottomRightX())
				tmpx = toShape.getBottomRightX() + Offset;
			else {
				tmpx = fromShape.getBottomRightX() + Offset;
			}
			if (toShape.getBottomRightY() < fromShape.getBottomRightY())
				tmpy = fromShape.getBottomRightY() + Offset;
			else {
				tmpy = toShape.getBottomRightY() + Offset;
			}
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(tmpx, fromY);
			Point p3 = new Point(tmpx, tmpy);
			Point p4 = new Point(toX, tmpy);
			Point p5 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
			list.add(p5);
		}

		return list;
	}
	
	/**
	 * 计算起始连接点在右边，最终连接点在左边的连线坐标
	 * @param fromShape
	 * @param toShape
	 * @param fromPort
	 * @param toPort
	 * @return
	 */
	public static List<Point> caculateRightLeft(Shape fromShape,
			Shape toShape, Port fromPort, Port toPort) {
		List<Point> list = new ArrayList<Point>();

		float fromX = (float) (fromShape.getX() + fromShape.getW()
				* fromPort.getX() + fromPort.getHorizontalOffset());
		float fromY = (float) (fromShape.getY() + fromShape.getH()
				* fromPort.getY() + fromPort.getVerticalOffset());
		float toX = (float) (toShape.getX() + toShape.getW() * toPort.getX() + toPort
				.getHorizontalOffset());
		float toY = (float) (toShape.getY() + toShape.getH() * toPort.getY() + toPort
				.getVerticalOffset());

		if (toShape.getX() - minLen > fromShape.getBottomRightX() + minLen) {
			if (toY == fromY) {
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(toX, toY);
				list.add(p1);
				list.add(p2);
			} else {
				float tmpx = (fromShape.getBottomRightX() + toShape.getX()) / 2.0F;
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(tmpx, fromY);
				Point p3 = new Point(tmpx, toY);
				Point p4 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
			}

		} else if ((toShape.getBottomRightY() + minLen < fromShape.getY()
				- minLen)
				|| (fromShape.getBottomRightY() + minLen < toShape.getY()
						- minLen)) {
			float tmpy = 0.0F;
			if (toShape.getBottomRightY() < fromShape.getBottomRightY())
				tmpy = (toShape.getBottomRightY() + fromShape.getY()) / 2.0F;
			else {
				tmpy = (toShape.getY() + fromShape.getBottomRightY()) / 2.0F;
			}
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(fromX + Offset, fromY);
			Point p3 = new Point(fromX + Offset, tmpy);
			Point p4 = new Point(toX - Offset, tmpy);
			Point p5 = new Point(toX - Offset, toY);
			Point p6 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
			list.add(p5);
			list.add(p6);
		} else {
			float tmpx = 0.0F;
			float tmpy = 0.0F;
			if (toShape.getBottomRightX() > fromShape.getBottomRightX())
				tmpx = toShape.getBottomRightX() + Offset;
			else {
				tmpx = fromShape.getBottomRightX() + Offset;
			}

			if (fromY >= toY) {
				if (toShape.getY() < fromShape.getY())
					tmpy = toShape.getY() - Offset;
				else {
					tmpy = fromShape.getY() - Offset;
				}
			} else if (toShape.getBottomRightY() > fromShape.getBottomRightY())
				tmpy = toShape.getBottomRightY() + Offset;
			else {
				tmpy = fromShape.getBottomRightY() + Offset;
			}

			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(tmpx, fromY);
			Point p3 = new Point(tmpx, tmpy);
			Point p4 = new Point(toX - Offset, tmpy);
			Point p5 = new Point(toX - Offset, toY);
			Point p6 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
			list.add(p5);
			list.add(p6);
		}

		return list;
	}
	
	/**
	 * 计算起始连接点在左边，最终连接点在头部的连线坐标
	 * @param fromShape
	 * @param toShape
	 * @param fromPort
	 * @param toPort
	 * @return
	 */
	public static List<Point> caculateLeftTop(Shape fromShape,
			Shape toShape, Port fromPort, Port toPort) {
		List<Point> list = new ArrayList<Point>();

		float fromX = (float) (fromShape.getX() + fromShape.getW()
				* fromPort.getX() + fromPort.getHorizontalOffset());
		float fromY = (float) (fromShape.getY() + fromShape.getH()
				* fromPort.getY() + fromPort.getVerticalOffset());
		float toX = (float) (toShape.getX() + toShape.getW() * toPort.getX() + toPort
				.getHorizontalOffset());
		float toY = (float) (toShape.getY() + toShape.getH() * toPort.getY() + toPort
				.getVerticalOffset());

		if (toShape.getBottomRightX() + minLen >= fromShape.getX() - minLen) {
			if (toShape.getY() - minLen >= fromShape.getBottomRightY() + minLen) {
				if (toX > fromShape.getX()) {
					float tmpy = (fromShape.getBottomRightY() + toShape.getY()) / 2.0F;
					Point p1 = new Point(fromX, fromY);
					Point p2 = new Point(fromX - Offset, fromY);
					Point p3 = new Point(fromX - Offset, tmpy);
					Point p4 = new Point(toX, tmpy);
					Point p5 = new Point(toX, toY);

					list.add(p1);
					list.add(p2);
					list.add(p3);
					list.add(p4);
					list.add(p5);
				} else {
					Point p1 = new Point(fromX, fromY);
					Point p2 = new Point(toX, fromY);
					Point p3 = new Point(toX, toY);

					list.add(p1);
					list.add(p2);
					list.add(p3);
				}
			} else if (toShape.getY() - minLen > fromY) {
				if (toX > fromShape.getX()) {
					float tmpx = 0.0F;
					float tmpy = 0.0F;
					if (toShape.getX() >= fromShape.getX())
						tmpx = fromShape.getX() - Offset;
					else {
						tmpx = toShape.getX() - Offset;
					}
					if (toShape.getY() < fromShape.getY())
						tmpy = toShape.getY() - Offset;
					else {
						tmpy = fromShape.getY() - Offset;
					}
					Point p1 = new Point(fromX, fromY);
					Point p2 = new Point(tmpx, fromY);
					Point p3 = new Point(tmpx, tmpy);
					Point p4 = new Point(toX, tmpy);
					Point p5 = new Point(toX, toY);

					list.add(p1);
					list.add(p2);
					list.add(p3);
					list.add(p4);
					list.add(p5);
				} else {
					Point p1 = new Point(fromX, fromY);
					Point p2 = new Point(toX, fromY);
					Point p3 = new Point(toX, toY);

					list.add(p1);
					list.add(p2);
					list.add(p3);
				}
			} else {
				float tmpx = 0.0F;
				float tmpy = 0.0F;
				if (toShape.getX() >= fromShape.getX())
					tmpx = fromShape.getX() - Offset;
				else {
					tmpx = toShape.getX() - Offset;
				}
				if (toShape.getY() < fromShape.getY())
					tmpy = toShape.getY() - Offset;
				else {
					tmpy = fromShape.getY() - Offset;
				}
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(tmpx, fromY);
				Point p3 = new Point(tmpx, tmpy);
				Point p4 = new Point(toX, tmpy);
				Point p5 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
				list.add(p5);
			}

		} else if (toShape.getY() - minLen >= fromY) {
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(toX, fromY);
			Point p3 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
		} else {
			float tmpx = (toShape.getBottomRightX() + fromShape.getX()) / 2.0F;
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(tmpx, fromY);
			Point p3 = new Point(tmpx, toY - Offset);
			Point p4 = new Point(toX, toY - Offset);
			Point p5 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
			list.add(p5);
		}

		return list;
	}
	
	/**
	 * 计算起始连接点在左边，最终连接点在右边的连线坐标
	 * @param fromShape
	 * @param toShape
	 * @param fromPort
	 * @param toPort
	 * @return
	 */
	public static List<Point> caculateLeftRight(Shape fromShape,
			Shape toShape, Port fromPort, Port toPort) {
		List<Point> list = new ArrayList<Point>();

		float fromX = (float) (fromShape.getX() + fromShape.getW()
				* fromPort.getX() + fromPort.getHorizontalOffset());
		float fromY = (float) (fromShape.getY() + fromShape.getH()
				* fromPort.getY() + fromPort.getVerticalOffset());
		float toX = (float) (toShape.getX() + toShape.getW() * toPort.getX() + toPort
				.getHorizontalOffset());
		float toY = (float) (toShape.getY() + toShape.getH() * toPort.getY() + toPort
				.getVerticalOffset());

		if (toShape.getBottomRightX() + minLen <= fromX - minLen) {
			if (fromY == toY) {
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(toX, toY);
				list.add(p1);
				list.add(p2);
			} else {
				float tmpx = (toShape.getBottomRightX() + fromShape.getX()) / 2.0F;
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(tmpx, fromY);
				Point p3 = new Point(tmpx, toY);
				Point p4 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
			}

		} else if ((toShape.getBottomRightY() + minLen <= fromShape.getY()
				- minLen)
				|| (toShape.getY() >= fromShape.getBottomRightY() + minLen)) {
			float tmpy = 0.0F;
			if (toShape.getBottomRightY() <= fromShape.getY())
				tmpy = (toShape.getBottomRightY() + fromShape.getY()) / 2.0F;
			else {
				tmpy = (toShape.getY() + fromShape.getBottomRightY()) / 2.0F;
			}
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(fromX - Offset, fromY);
			Point p3 = new Point(fromX - Offset, tmpy);
			Point p4 = new Point(toX + Offset, tmpy);
			Point p5 = new Point(toX + Offset, toY);
			Point p6 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
			list.add(p5);
			list.add(p6);
		} else {
			float tmpx = 0.0F;
			float tmpy = 0.0F;

			if (toShape.getX() < fromShape.getX())
				tmpx = toShape.getX() - Offset;
			else {
				tmpx = fromX - Offset;
			}

			if (fromY > toY) {
				if (toShape.getY() < fromShape.getY())
					tmpy = toShape.getY() - Offset;
				else {
					tmpy = fromShape.getY() - Offset;
				}

			} else if (toShape.getBottomRightY() > fromShape.getBottomRightY())
				tmpy = toShape.getBottomRightY() + Offset;
			else {
				tmpy = fromShape.getBottomRightY() + Offset;
			}

			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(tmpx, fromY);
			Point p3 = new Point(tmpx, tmpy);
			Point p4 = new Point(toX + Offset, tmpy);
			Point p5 = new Point(toX + Offset, toY);
			Point p6 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
			list.add(p5);
			list.add(p6);
		}

		return list;
	}
	
	/**
	 * 计算起始连接点在左边，最终连接点在底部的连线坐标
	 * @param fromShape
	 * @param toShape
	 * @param fromPort
	 * @param toPort
	 * @return
	 */
	public static List<Point> caculateLeftBottom(Shape fromShape,
			Shape toShape, Port fromPort, Port toPort) {
		List<Point> list = new ArrayList<Point>();

		float fromX = (float) (fromShape.getX() + fromShape.getW()
				* fromPort.getX() + fromPort.getHorizontalOffset());
		float fromY = (float) (fromShape.getY() + fromShape.getH()
				* fromPort.getY() + fromPort.getVerticalOffset());
		float toX = (float) (toShape.getX() + toShape.getW() * toPort.getX() + toPort
				.getHorizontalOffset());
		float toY = (float) (toShape.getY() + toShape.getH() * toPort.getY() + toPort
				.getVerticalOffset());

		if (toShape.getBottomRightX() + minLen < fromShape.getX() - minLen) {
			if (toShape.getBottomRightY() + minLen <= fromY) {
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(toX, fromY);
				Point p3 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
			} else {
				float tmpx = (toShape.getBottomRightX() + fromShape.getX()) / 2.0F;
				float tmpy = toShape.getBottomRightY() + Offset;
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(tmpx, fromY);
				Point p3 = new Point(tmpx, tmpy);
				Point p4 = new Point(toX, tmpy);
				Point p5 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
				list.add(p5);
			}

		} else if (toShape.getBottomRightY() + minLen < fromShape.getY()
				- minLen) {
			if (toX <= fromShape.getX()) {
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(toX, fromY);
				Point p3 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
			} else {
				float tmpy = (toShape.getBottomRightY() + fromShape.getY()) / 2.0F;
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(fromX - Offset, fromY);
				Point p3 = new Point(fromX - Offset, tmpy);
				Point p4 = new Point(toX, tmpy);
				Point p5 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
				list.add(p5);
			}
		} else if ((toShape.getBottomRightY() + minLen <= fromY)
				&& (toX < fromShape.getX() - minLen)) {
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(toX, fromY);
			Point p3 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
		} else {
			float tmpx = 0.0F;
			float tmpy = 0.0F;

			if (toShape.getX() < fromShape.getX())
				tmpx = toShape.getX() - Offset;
			else {
				tmpx = fromShape.getX() - Offset;
			}
			if (toShape.getBottomRightY() < fromShape.getBottomRightY())
				tmpy = fromShape.getBottomRightY() + Offset;
			else {
				tmpy = toShape.getBottomRightY() + Offset;
			}
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(tmpx, fromY);
			Point p3 = new Point(tmpx, tmpy);
			Point p4 = new Point(toX, tmpy);
			Point p5 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
			list.add(p5);
		}

		return list;
	}

	/**
	 * 计算起始连接点在左边，最终连接点在左边的连线坐标
	 * @param fromShape
	 * @param toShape
	 * @param fromPort
	 * @param toPort
	 * @return
	 */
	public static List<Point> caculateLeftLeft(Shape fromShape,
			Shape toShape, Port fromPort, Port toPort) {
		List<Point> list = new ArrayList<Point>();

		float fromX = (float) (fromShape.getX() + fromShape.getW()
				* fromPort.getX() + fromPort.getHorizontalOffset());
		float fromY = (float) (fromShape.getY() + fromShape.getH()
				* fromPort.getY() + fromPort.getVerticalOffset());
		float toX = (float) (toShape.getX() + toShape.getW() * toPort.getX() + toPort
				.getHorizontalOffset());
		float toY = (float) (toShape.getY() + toShape.getH() * toPort.getY() + toPort
				.getVerticalOffset());

		if (toShape.getBottomRightX() + minLen < fromShape.getX() - minLen) {
			if ((toShape.getBottomRightY() + minLen < fromY)
					|| (toShape.getY() - minLen > fromY)) {
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(toX - Offset, fromY);
				Point p3 = new Point(toX - Offset, toY);
				Point p4 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
			} else {
				float tmpx = (toShape.getBottomRightX() + fromShape.getX()) / 2.0F;
				float tmpy = 0.0F;

				if (toY >= fromY)
					tmpy = toShape.getY() - Offset;
				else {
					tmpy = toShape.getBottomRightY() + Offset;
				}
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(tmpx, fromY);
				Point p3 = new Point(tmpx, tmpy);
				Point p4 = new Point(toX - Offset, tmpy);
				Point p5 = new Point(toX - Offset, toY);
				Point p6 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
				list.add(p5);
				list.add(p6);
			}

		} else if (toShape.getX() - minLen < fromShape.getBottomRightX()
				+ minLen) {
			float tmpx = 0.0F;
			if (toShape.getX() < fromShape.getX())
				tmpx = toShape.getX() - Offset;
			else {
				tmpx = fromShape.getX() - Offset;
			}
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(tmpx, fromY);
			Point p3 = new Point(tmpx, toY);
			Point p4 = new Point(toX, toY);
			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
		} else if ((toY < fromShape.getY() - minLen)
				|| (toY > fromShape.getBottomRightY() + minLen)) {
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(fromX - Offset, fromY);
			Point p3 = new Point(fromX - Offset, toY);
			Point p4 = new Point(toX, toY);
			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
		} else {
			float tmpx = (fromShape.getBottomRightX() + toShape.getX()) / 2.0F;
			float tmpy = 0.0F;

			if (fromY > toY)
				tmpy = fromShape.getY() - Offset;
			else {
				tmpy = fromShape.getBottomRightY() + Offset;
			}
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(fromX - Offset, fromY);
			Point p3 = new Point(fromX - Offset, tmpy);
			Point p4 = new Point(tmpx, tmpy);
			Point p5 = new Point(tmpx, toY);
			Point p6 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
			list.add(p5);
			list.add(p6);
		}

		return list;
	}
	
	/**
	 * 计算起始连接点在底部，最终连接点在头部的连线坐标
	 * @param fromShape
	 * @param toShape
	 * @param fromPort
	 * @param toPort
	 * @return
	 */
	public static List<Point> caculateBottomTop(Shape fromShape,
			Shape toShape, Port fromPort, Port toPort) {
		List<Point> list = new ArrayList<Point>();

		float fromX = (float) (fromShape.getX() + fromShape.getW()
				* fromPort.getX() + fromPort.getHorizontalOffset());
		float fromY = (float) (fromShape.getY() + fromShape.getH()
				* fromPort.getY() + fromPort.getVerticalOffset());
		float toX = (float) (toShape.getX() + toShape.getW() * toPort.getX() + toPort
				.getHorizontalOffset());
		float toY = (float) (toShape.getY() + toShape.getH() * toPort.getY() + toPort
				.getVerticalOffset());

		if (toShape.getY() - minLen < fromShape.getBottomRightY() + minLen) {
			if ((toShape.getBottomRightX() + minLen < fromShape.getX() - minLen)
					|| (toShape.getX() - minLen > fromShape.getBottomRightX()
							+ minLen)) {
				float tmpx = 0.0F;
				if (toShape.getBottomRightX() < fromShape.getX())
					tmpx = (toShape.getBottomRightX() + fromShape.getX()) / 2.0F;
				else {
					tmpx = (toShape.getX() + fromShape.getBottomRightX()) / 2.0F;
				}
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(fromX, fromY + Offset);
				Point p3 = new Point(tmpx, fromY + Offset);
				Point p4 = new Point(tmpx, toY - Offset);
				Point p5 = new Point(toX, toY - Offset);
				Point p6 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
				list.add(p5);
				list.add(p6);
			} else {
				float tmpx = 0.0F;
				float tmpy = 0.0F;

				if (toX > fromX) {
					if (toShape.getBottomRightX() > fromShape.getBottomRightX())
						tmpx = toShape.getBottomRightX() + Offset;
					else {
						tmpx = fromShape.getBottomRightX() + Offset;
					}
				} else if (toShape.getX() < fromShape.getX())
					tmpx = toShape.getX() - Offset;
				else {
					tmpx = fromShape.getX() - Offset;
				}

				if (toShape.getBottomRightY() > fromShape.getBottomRightY())
					tmpy = toShape.getBottomRightY() + Offset;
				else {
					tmpy = fromShape.getBottomRightY() + Offset;
				}
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(fromX, tmpy);
				Point p3 = new Point(tmpx, tmpy);
				Point p4 = new Point(tmpx, toY - Offset);
				Point p5 = new Point(toX, toY - Offset);
				Point p6 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
				list.add(p5);
				list.add(p6);
			}

		} else if (toX == fromX) {
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(toX, toY);
			list.add(p1);
			list.add(p2);
		} else {
			float tmpy = (fromShape.getBottomRightY() + toShape.getY()) / 2.0F;
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(fromX, tmpy);
			Point p3 = new Point(toX, tmpy);
			Point p4 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
		}

		return list;
	}
	
	/**
	 * 计算起始连接点在底部，最终连接点在右边的连线坐标
	 * @param fromShape
	 * @param toShape
	 * @param fromPort
	 * @param toPort
	 * @return
	 */
	public static List<Point> caculateBottomRight(Shape fromShape,
			Shape toShape, Port fromPort, Port toPort) {
		List<Point> list = new ArrayList<Point>();

		float fromX = (float) (fromShape.getX() + fromShape.getW()
				* fromPort.getX() + fromPort.getHorizontalOffset());
		float fromY = (float) (fromShape.getY() + fromShape.getH()
				* fromPort.getY() + fromPort.getVerticalOffset());
		float toX = (float) (toShape.getX() + toShape.getW() * toPort.getX() + toPort
				.getHorizontalOffset());
		float toY = (float) (toShape.getY() + toShape.getH() * toPort.getY() + toPort
				.getVerticalOffset());

		if (toY >= fromShape.getBottomRightY() + minLen) {
			if (toShape.getBottomRightX() + minLen <= fromX) {
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(fromX, toY);
				Point p3 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
			} else if (toShape.getY() - minLen > fromShape.getBottomRightY()
					+ minLen) {
				float tmpy = (fromShape.getBottomRightY() + toShape.getY()) / 2.0F;
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(fromX, tmpy);
				Point p3 = new Point(toX + Offset, tmpy);
				Point p4 = new Point(toX + Offset, toY);
				Point p5 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
				list.add(p5);
			} else {
				float tmpy = 0.0F;
				if (toShape.getBottomRightY() > fromShape.getBottomRightY())
					tmpy = toShape.getBottomRightY() + Offset;
				else {
					tmpy = fromShape.getBottomRightY() + Offset;
				}
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(fromX, tmpy);
				Point p3 = new Point(toX + Offset, tmpy);
				Point p4 = new Point(toX + Offset, toY);
				Point p5 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
				list.add(p5);
			}

		} else if (toShape.getBottomRightX() + minLen < fromShape.getX()
				- minLen) {
			float tmpx = (toShape.getBottomRightX() + fromShape.getX()) / 2.0F;
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(fromX, fromY + Offset);
			Point p3 = new Point(tmpx, fromY + Offset);
			Point p4 = new Point(tmpx, toY);
			Point p5 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
			list.add(p5);
		} else {
			float tmpx = 0.0F;
			float tmpy = 0.0F;
			if (toShape.getBottomRightX() > fromShape.getBottomRightX())
				tmpx = toShape.getBottomRightX() + Offset;
			else {
				tmpx = fromShape.getBottomRightX() + Offset;
			}
			if (toShape.getBottomRightY() > fromShape.getBottomRightY())
				tmpy = toShape.getBottomRightY() + Offset;
			else {
				tmpy = fromShape.getBottomRightY() + Offset;
			}
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(fromX, tmpy);
			Point p3 = new Point(tmpx, tmpy);
			Point p4 = new Point(tmpx, toY);
			Point p5 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
			list.add(p5);
		}

		return list;
	}
	
	/**
	 * 计算起始连接点在底部，最终连接点在底部的连线坐标
	 * @param fromShape
	 * @param toShape
	 * @param fromPort
	 * @param toPort
	 * @return
	 */
	public static List<Point> caculateBottomBottom(Shape fromShape,
			Shape toShape, Port fromPort, Port toPort) {
		List<Point> list = new ArrayList<Point>();

		float fromX = (float) (fromShape.getX() + fromShape.getW()
				* fromPort.getX() + fromPort.getHorizontalOffset());
		float fromY = (float) (fromShape.getY() + fromShape.getH()
				* fromPort.getY() + fromPort.getVerticalOffset());
		float toX = (float) (toShape.getX() + toShape.getW() * toPort.getX() + toPort
				.getHorizontalOffset());
		float toY = (float) (toShape.getY() + toShape.getH() * toPort.getY() + toPort
				.getVerticalOffset());

		if (toY + minLen < fromShape.getY() - minLen) {
			if ((toX < fromShape.getX() - minLen)
					|| (toX > fromShape.getBottomRightX() + minLen)) {
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(fromX, fromY + Offset);
				Point p3 = new Point(toX, fromY + Offset);
				Point p4 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
			} else {
				float tmpy = (toShape.getBottomRightY() + fromShape.getY()) / 2.0F;
				float tmpx = 0.0F;
				if (toX < fromX)
					tmpx = fromShape.getX() - Offset;
				else {
					tmpx = fromShape.getBottomRightX() + Offset;
				}
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(fromX, fromY + Offset);
				Point p3 = new Point(tmpx, fromY + Offset);
				Point p4 = new Point(tmpx, tmpy);
				Point p5 = new Point(toX, tmpy);
				Point p6 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
				list.add(p5);
				list.add(p6);
			}

		} else if ((toShape.getX() - minLen > fromX)
				|| (toShape.getBottomRightX() + minLen < fromX)) {
			float tmpy = 0.0F;
			if (toShape.getBottomRightY() > fromShape.getBottomRightY())
				tmpy = toShape.getBottomRightY() + Offset;
			else {
				tmpy = fromShape.getBottomRightY() + Offset;
			}
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(fromX, tmpy);
			Point p3 = new Point(toX, tmpy);
			Point p4 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
		} else if (toShape.getY() - minLen <= fromShape.getBottomRightY()
				+ minLen) {
			float tmpy = 0.0F;
			if (toShape.getBottomRightY() > fromShape.getBottomRightY())
				tmpy = toShape.getBottomRightY() + Offset;
			else {
				tmpy = fromShape.getBottomRightY() + Offset;
			}
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(fromX, tmpy);
			Point p3 = new Point(toX, tmpy);
			Point p4 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
		} else {
			float tmpx = 0.0F;
			float tmpy = (toShape.getY() + fromY) / 2.0F;
			if (toX < fromX)
				tmpx = toShape.getBottomRightX() + Offset;
			else {
				tmpx = toShape.getX() - Offset;
			}
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(fromX, tmpy);
			Point p3 = new Point(tmpx, tmpy);
			Point p4 = new Point(tmpx, toY + Offset);
			Point p5 = new Point(toX, toY + Offset);
			Point p6 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
			list.add(p5);
			list.add(p6);
		}

		return list;
	}
	
	/**
	 * 计算起始连接点在底部，最终连接点在左边的连线坐标
	 * @param fromShape
	 * @param toShape
	 * @param fromPort
	 * @param toPort
	 * @return
	 */
	public static List<Point> caculateBottomLeft(Shape fromShape,
			Shape toShape, Port fromPort, Port toPort) {
		List<Point> list = new ArrayList<Point>();

		float fromX = (float) (fromShape.getX() + fromShape.getW()
				* fromPort.getX() + fromPort.getHorizontalOffset());
		float fromY = (float) (fromShape.getY() + fromShape.getH()
				* fromPort.getY() + fromPort.getVerticalOffset());
		float toX = (float) (toShape.getX() + toShape.getW() * toPort.getX() + toPort
				.getHorizontalOffset());
		float toY = (float) (toShape.getY() + toShape.getH() * toPort.getY() + toPort
				.getVerticalOffset());

		if (toY >= fromShape.getBottomRightY() + minLen) {
			if (toShape.getX() - minLen >= fromX) {
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(fromX, toY);
				Point p3 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
			} else if (toShape.getY() - minLen > fromShape.getBottomRightY()
					+ minLen) {
				float tmpy = (fromShape.getBottomRightY() + toShape.getY()) / 2.0F;
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(fromX, tmpy);
				Point p3 = new Point(toX - Offset, tmpy);
				Point p4 = new Point(toX - Offset, toY);
				Point p5 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
				list.add(p5);
			} else {
				float tmpy = 0.0F;
				if (toShape.getBottomRightY() > fromShape.getBottomRightY())
					tmpy = toShape.getBottomRightY() + Offset;
				else {
					tmpy = fromShape.getBottomRightY() + Offset;
				}
				Point p1 = new Point(fromX, fromY);
				Point p2 = new Point(fromX, tmpy);
				Point p3 = new Point(toX - Offset, tmpy);
				Point p4 = new Point(toX - Offset, toY);
				Point p5 = new Point(toX, toY);

				list.add(p1);
				list.add(p2);
				list.add(p3);
				list.add(p4);
				list.add(p5);
			}

		} else if (toShape.getX() - minLen > fromShape.getBottomRightX()
				+ minLen) {
			float tmpx = (fromShape.getBottomRightX() + toShape.getX()) / 2.0F;
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(fromX, fromY + Offset);
			Point p3 = new Point(tmpx, fromY + Offset);
			Point p4 = new Point(tmpx, toY);
			Point p5 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
			list.add(p5);
		} else {
			float tmpx = 0.0F;
			float tmpy = 0.0F;
			if (toShape.getX() > fromShape.getX())
				tmpx = fromShape.getX() - Offset;
			else {
				tmpx = toShape.getX() - Offset;
			}
			if (toShape.getBottomRightY() > fromShape.getBottomRightY())
				tmpy = toShape.getBottomRightY() + Offset;
			else {
				tmpy = fromShape.getBottomRightY() + Offset;
			}
			Point p1 = new Point(fromX, fromY);
			Point p2 = new Point(fromX, tmpy);
			Point p3 = new Point(tmpx, tmpy);
			Point p4 = new Point(tmpx, toY);
			Point p5 = new Point(toX, toY);

			list.add(p1);
			list.add(p2);
			list.add(p3);
			list.add(p4);
			list.add(p5);
		}

		return list;
	}

	private static String getPointXml(List<Point> list) {
		StringBuffer sb = new StringBuffer();
		for (Point p : list) {
			sb.append("\n<omgdi:waypoint x=\"" + p.getX() + "\" y=\""
					+ p.getY() + "\"></omgdi:waypoint>\n");
		}
		return sb.toString();
	}

	public static String calcLabelPosition(String label) {
		float labelLen = 0.0F;
		for (int i = 0; i < label.length(); i++) {
			if (label.charAt(i) > 'ÿ')
				labelLen += 2.0F;
			else if (Character.isUpperCase(label.charAt(i)))
				labelLen = (float) (labelLen + 1.5D);
			else {
				labelLen += 1.0F;
			}
		}

		int x = (int) (labelLen > 16.0F ? -50.0F
				: -(labelLen / 16.0F + 1.0F) * 100.0F / 2.0F);
		int y = 0;
		int width = 100;
		int height = (int) ((labelLen / 16.0F + 1.0F) * 14.0F);
		StringBuffer position = new StringBuffer();
		position.append(" <omgdc:Bounds ");
		position.append("x=\"" + x + "\" ");
		position.append("y=\"" + y + "\" ");
		position.append("width=\"" + width + "\" ");
		position.append("height=\"" + height + "\">");
		position.append("</omgdc:Bounds>");

		return position.toString();
	}
}
