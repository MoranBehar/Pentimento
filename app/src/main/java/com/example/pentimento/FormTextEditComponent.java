package com.example.pentimento;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FormTextEditComponent extends LinearLayout {
    private EditText editText;
    private TextView topLabel;

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

        editText = findViewById(R.id.editTextField);
        topLabel = findViewById(R.id.editTextTopLabel);

        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
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

}
