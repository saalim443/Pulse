package codeflies.com.pulse.Helpers;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import codeflies.com.pulse.databinding.CustomProgressLayoutBinding;


public class ProgressDisplay extends Dialog {


    public ProgressDisplay(Context context) {
        super(context);
    }


    CustomProgressLayoutBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        binding=CustomProgressLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setCanceledOnTouchOutside(false);
    }

    // Set a custom message for the dialog
    public void setMessage(String message) {
        binding.customMessage.setText(message);
    }


}
