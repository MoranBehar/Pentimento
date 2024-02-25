package com.example.pentimento;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class FormTextEditComponent extends LinearLayout {
    private EditText editText;
    private TextView topLabel, errorMsg;
    private LinearLayout editArea;

    private Context myContext;

    public FormTextEditComponent(Context context) {
        super(context);
        init(context, null);
    }

    public FormTextEditComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FormTextEditComponent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        LayoutInflater.from(context).inflate(R.layout.component_form_text_edit, this, true);

        myContext = context;

        editText = findViewById(R.id.editTextField);
        topLabel = findViewById(R.id.editTextTopLabel);
        errorMsg = findViewById(R.id.errorMessage);
        editArea = findViewById(R.id.editArea);

        editText.setOnFocusChangeListener(this::handleFocusChanges);

        handleAttributes(attrs);
    }

    private void handleFocusChanges(View view, boolean inFocus) {
        if (inFocus) {
            editArea.setBackground(ContextCompat.getDrawable(myContext, R.drawable.border_thin_focus));
        } else {
            editArea.setBackground(ContextCompat.getDrawable(myContext, R.drawable.border_thin));
        }
    }

    private void handleAttributes(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = myContext.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.FormTextEditComponent,
                    0, 0);

            try {
                String hint = a.getString(R.styleable.FormTextEditComponent_customHint);
                String topLabelText = a.getString(R.styleable.FormTextEditComponent_topLabelText);

                editText.setHint(hint);
                topLabel.setText(topLabelText);
            } finally {
                a.recycle();
            }
        }
    }

    public EditText getEditText() {
        return editText;
    }

    public void setError(String message) {
        errorMsg.setText(message);
        errorMsg.setVisibility(View.VISIBLE);
        editArea.setBackground(ContextCompat.getDrawable(myContext, R.drawable.border_thin_error));
    }

    public void clearError() {
        errorMsg.setText("");
        errorMsg.setVisibility(View.GONE);
        editArea.setBackground(ContextCompat.getDrawable(myContext, R.drawable.border_thin));
    }

}
