package Attacks;

import Entities.MainCharacter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class RangeAttack extends Actor {
	private float radius;
	private Vector2 position;
	private boolean visible;
	private MainCharacter mainpj;
	private ShapeRenderer circle;
	
	public RangeAttack(MainCharacter mainpj) {
		visible = true;
		this.mainpj = mainpj;
		radius = 95;
		position = new Vector2();
		circle = new ShapeRenderer();
		circle.setColor(0.8f, 0.93f, 0.96f, 0.8f);
	}
	
	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible (boolean visible) {
		this.visible = visible;
	}
	
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		this.position.x = x;
		this.position.y = y;
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		if (visible) {
			batch.end();
			Gdx.gl.glEnable(GL10.GL_LINE_SMOOTH);
			Gdx.gl.glEnable(GL10.GL_BLEND);
			    Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
				circle.begin(ShapeType.Filled);
				circle.setColor(0.8f, 0.93f, 0.96f, 0.5f);
			    circle.circle(position.x, position.y, radius);
			    circle.end();
			    circle.begin(ShapeType.Line);
			    circle.setColor(0.4f, 0.6f, 1f, 0.9f);
			    circle.circle(position.x, position.y, radius);
			    circle.end();
		    Gdx.gl.glDisable(GL10.GL_BLEND);
		    batch.begin();
		}
	}
    
    @Override
    public void act(float delta) {
    	if (visible) {
    		setPosition(mainpj.getCenterX(), mainpj.getCenterY());
    	}
    }
    
    public void dispose() {
    	circle.dispose();
    }
}
