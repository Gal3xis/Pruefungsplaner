package com.Fachhochschulebib.fhb.pruefungsplaner;

import static android.support.v7.widget.helper.ItemTouchHelper.*;

import android.content.res.Resources;
import android.graphics.Canvas;
        import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
        import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.MotionEvent;
        import android.view.View;

enum ButtonsState {
    GONE,
    LEFT_VISIBLE,
    RIGHT_VISIBLE
}

public class SwipeController extends Callback {

    private boolean swipeBack = false;

    private ButtonsState buttonShowedState = ButtonsState.GONE;

    private RectF buttonInstance = null;

    private RecyclerView.ViewHolder currentItemViewHolder = null;

    private SwipeControllerActions buttonsActions;

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private static final float buttonWidth = getScreenWidth()-100;

    public SwipeController(SwipeControllerActions buttonsActions) {
        this.buttonsActions = buttonsActions;
    }



    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, LEFT );
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }




    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        if (swipeBack) {
            swipeBack = buttonShowedState != ButtonsState.GONE;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ACTION_STATE_SWIPE) {
            if (buttonShowedState != ButtonsState.GONE) {
                if (buttonShowedState == ButtonsState.LEFT_VISIBLE) dX = Math.max(dX, 300);
                if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {dX = Math.min(dX, -buttonWidth);}
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
            else {
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }

        if (buttonShowedState == ButtonsState.GONE) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
        currentItemViewHolder = viewHolder;
    }

    private void setTouchListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
                if (swipeBack) {
                    if (dX < -100) {buttonShowedState = ButtonsState.RIGHT_VISIBLE; }

                    if (buttonShowedState != ButtonsState.GONE) {
                        buttonsActions.onLeftClicked(viewHolder.getAdapterPosition());
                        setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        setItemsClickable(recyclerView, false);
                    }
                }
                return false;
            }
        });
    }

    private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {


                    setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                }
                return false;
            }
        });
    }


    private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    SwipeController.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
                    recyclerView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            return false;
                        }
                    });
                    setItemsClickable(recyclerView, true);
                    swipeBack = false;

                    if (buttonsActions != null && buttonInstance != null && buttonInstance.contains(event.getX(), event.getY())) {
                        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
                            buttonsActions.onRightClicked(viewHolder.getAdapterPosition());
                        }
                        else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
                            buttonsActions.onRightClicked(viewHolder.getAdapterPosition());
                        }
                    }
                    buttonShowedState = ButtonsState.GONE;
                    currentItemViewHolder = null;
                }
                return false;
            }
        });
    }

    private void setItemsClickable(RecyclerView recyclerView, boolean isClickable) {
        for (int i = 0; i < recyclerView.getChildCount(); ++i) {
            recyclerView.getChildAt(i).setClickable(isClickable);
        }
    }

    private void drawButtons2(Canvas c, RecyclerView.ViewHolder viewHolder,String s) {
        float buttonWidthWithoutPadding = buttonWidth - 20;
        float corners = 16;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        RectF leftButton = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + buttonWidthWithoutPadding, itemView.getBottom());
        //p.setColor(Color.BLUE);
        //c.drawRoundRect(leftButton, corners, corners, p);
        //drawText("EDIT", c, leftButton, p);

        RectF rightButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getBottom() - 900, itemView.getRight(), itemView.getBottom());
        //p.setColor(Color.DKGRAY);
        String color = "#06ABF9";
        String color2 = "#43B3F9";
        p.setShader(new LinearGradient(0, 0, 0,itemView.getScaleY()*2,Color.parseColor(color2), Color.parseColor(color) , Shader.TileMode.MIRROR));
        c.drawRoundRect(rightButton, corners, corners, p);
        drawText(s, c, rightButton, p);


        buttonInstance = null;
        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
            buttonInstance = leftButton;
        }
        else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
            buttonInstance = leftButton;
        }
    }


    private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder,String s) {
        float buttonWidthWithoutPadding = buttonWidth - 20;
        float corners = 16;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        RectF leftButton = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + buttonWidthWithoutPadding, itemView.getBottom());
        //p.setColor(Color.BLUE);
        //c.drawRoundRect(leftButton, corners, corners, p);
        //drawText("EDIT", c, leftButton, p);

        RectF rightButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop(), itemView.getRight(), itemView.getTop()+ 1000);
        //p.setColor(Color.DKGRAY);
        String color = "#06ABF9";
        String color2 = "#43B3F9";
        p.setShader(new LinearGradient(0, 0, 0,itemView.getScaleY()*2,Color.parseColor(color2), Color.parseColor(color) , Shader.TileMode.MIRROR));
        c.drawRoundRect(rightButton, corners, corners, p);
        drawText(s, c, rightButton, p);

        buttonInstance = null;
        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
            buttonInstance = leftButton;
        }
        else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
            buttonInstance = leftButton;
        }
    }

    private void drawButtonsup(Canvas c, final RecyclerView.ViewHolder viewHolder) {
        float buttonWidthWithoutPadding = buttonWidth - 20;
        float corners = 16;

        View itemView = viewHolder.itemView;
        Paint p = new Paint();

        //RectF leftButton = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getLeft() + buttonWidthWithoutPadding, itemView.getBottom());
        //p.setColor(Color.BLUE);
        //c.drawRoundRect(leftButton, corners, corners, p);
        //drawText("EDIT", c, leftButton, p);

        RectF leftButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding, itemView.getTop()-((itemView.getBottom() - itemView.getTop())*2), itemView.getRight(), itemView.getHeight()*1);
        p.setColor(Color.BLUE);
        c.drawRoundRect(leftButton, corners, corners, p);
        drawText("INFOS", c, leftButton, p);

        itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });
        buttonInstance = null;
        if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
            buttonInstance = leftButton;
        }
        else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
            buttonInstance = leftButton;
        }
    }

    private void drawText(String text, Canvas c, RectF button, Paint p) {
        float textSize = 60;
        p.setColor(Color.WHITE);
        p.setAntiAlias(true);
        p.setTextSize(textSize);

        float textWidth = p.measureText(text);
       // c.drawText(text, button.centerX(), button.centerY(), p);


        TextPaint mTextPaint=new TextPaint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(textSize);
        StaticLayout mTextLayout = new StaticLayout(text, mTextPaint, c.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        c.save();
        // calculate x and y position where your text will be placed

       float textX = button.left + 5 ;
        float textY = button.top + 5 ;

        c.translate(textX, textY);
        mTextLayout.draw(c);
        c.restore();
    }

    public void onDraw(Canvas c,String s) {
        if (currentItemViewHolder != null) {
            drawButtons(c, currentItemViewHolder,s);
        }
    }

    public void onDraw2(Canvas c,String s) {
        if (currentItemViewHolder != null) {
            drawButtons2(c, currentItemViewHolder,s);
        }
    }

    public void onDrawup(Canvas c) {
        if (currentItemViewHolder != null) {
            drawButtonsup(c, currentItemViewHolder);
        }
    }

    }

