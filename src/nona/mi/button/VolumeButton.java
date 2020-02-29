import java.awt.*;

public class Volume {

	private int gaugeX;
	private int gaugeY;
	private int gaugeWidth;
	private int gaugeHeight;

	private int pointerX;
	private int pointerY;
	private int pointerWidth;
	private int pointerHeight;

	private int areaX;
	private int areaWidth;
	private int areaY;
	private int areaHeight;

	private float minRange;
	private float maxRange;
	private float zeroBasedRange;

	private float value;
	private int valueX;
	private int valueY;

	

	public void definePointer(int pointerX, int pointerY, int pointerWidth, int pointerHeight) {
		this.pointerX = pointerX;
		this.pointerY = pointerY;
		this.pointerWidth = pointerWidth;
		this.pointerHeight = pointerHeight;
	}

	public void defineGauge(int gaugeX, int gaugeY, int gaugeWidth, int gaugeHeight) {
		this.gaugeX = gaugeX;
		this.gaugeY = gaugeY;
		this.gaugeWidth = gaugeWidth + pointerWidth; //para que o pointer consiga chegar ao valor maximo, pois o ponto de referencia dele eh o superior direito
		this.gaugeHeight = gaugeHeight;
	}

	public void defineArea(int areaX, int areaY, int areaWidth, int areaHeight) {
		this.areaX = areaX;
		this.areaY = areaY;
		this.areaWidth = areaWidth;
		this.areaHeight = areaHeight;
	}

	public void defineRange(float minRange, float maxRange) {

		//regra de 3 para aumentar a range de acordo com o tanto
		//que foi aumentado no gauge com a soma do ponteiro.

		//ex:
		//300(gaugeWidth)          ------> 310 (gaugeWidth + pointerWidth)
		//255(maxRange - minRange) ------> x

		int originalGaugeWidth = gaugeWidth - pointerWidth;
		zeroBasedRange = (((maxRange - minRange) * gaugeWidth) / originalGaugeWidth);

		this.minRange = minRange;
		this.maxRange = maxRange;
	}

	public void defineRenderValueXY(int valueX, int  valueY) {
		this.valueX = valueX;
		this.valueY = valueY;
	}

	public void defineInitialValue(float value) {
		this.value = value;
		pointerX = (int)(((value * gaugeWidth) / zeroBasedRange) + gaugeX);
	}

	public void update(int mouseX, int mouseY) {
		pointerX = ((mouseX) - (pointerWidth / 2)); //esse eh para deixar na posicao certa de render
		if (pointerX < gaugeX) {
			pointerX = gaugeX;
		} else if (pointerX > ((gaugeWidth + gaugeX) - pointerWidth)) {
			pointerX = ((gaugeWidth + gaugeX) - pointerWidth);
		}
		value = (((pointerX - gaugeX) * zeroBasedRange) / gaugeWidth);
		value = minRange + value;
	}

	public boolean isOnArea(int mouseX, int mouseY) {
		return ((mouseX > areaX) && (mouseX < (areaWidth + areaX)) && (mouseY > areaY) && (mouseY < areaHeight + areaY));
	}

	public float getValue() {
		return value;
	}

	public int getPointerX() {
		return pointerX;
	}

	public void render(Graphics g) {
		renderArea(g);
		renderGauge(g);
		renderPointer(g);
		renderValue(g);
	}

	private void renderArea(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(areaX, areaY, areaWidth, areaHeight);
	}

	private void renderGauge(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(gaugeX, gaugeY, gaugeWidth, gaugeHeight);
	}

	private void renderPointer(Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(pointerX, pointerY, pointerWidth, pointerHeight);
	}

	private void renderValue(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawString("Value: " + (int)(value), valueX, valueY);
	}

}