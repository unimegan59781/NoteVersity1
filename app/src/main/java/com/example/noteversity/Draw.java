package com.example.noteversity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;


public class Draw extends View {

    public LayoutParams layout; // sets view attributes
    private Path penPath = new Path(); // tracks pen path
    private Paint pen = new Paint(); // sets paint of pen to draw


    // constructor for layout of drawing on note page
    public Draw(Context context) {
        super(context);

        layout = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        pen.setAntiAlias(true); // allows drawing sets pen usage to true
        pen.setStyle(Paint.Style.STROKE);
        pen.setStrokeWidth(5f); // set variables to change factors
        pen.setColor(Color.BLACK);

        }

        // tracks pen movement and creates path every time user interacts with screen
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float pX = event.getX();
            float pY = event.getY();

            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN: // when finger touch screen changes co-ordinates
                    penPath.moveTo(pX, pY);
                    return true;
                case MotionEvent.ACTION_MOVE: // when move on screen draws using where finger been
                    penPath.lineTo(pX, pY);
                    break;
                default:  // error case
                    return false;
            }

            postInvalidate();
            return false;
        }

        // draws using set pen and path created by user intercations
        @Override
        protected void onDraw(Canvas canvas){
            canvas.drawPath(penPath, pen);
        }

        // cleans path on page and resets draw
        public void clean(){ // clean note page
        penPath.reset();
        invalidate();
        }

}
