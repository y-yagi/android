import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class Circle {
	public int x; // x座標
	public int y; // y座標
	public int size; // 円のサイズ
	Paint p; 

	public Circle(int x, int y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
		Paint p = new Paint();
		p.setColor(Color.RED);
		// p.setStyle(Paint.Style.FILL);
		p.setStyle(Paint.Style.STROKE);
		p.setStrokeWidth(5);
	}
	
	public Canvas setPaint(Canvas canvas) {
		canvas.drawCircle(200, 200, 80, p);
		return canvas;
	}
	
}
