package com.kyboon.kindlepusher;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kyboon.kindlepusher.MailSender.MailSender;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        Button btn = rootView.findViewById(R.id.btnSend);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAndReadTxt();
            }
        });
        return rootView;

    }

    private void saveAndReadTxt() {
//        String ster = "Test asduifghaduifg\n huiadshfuiasdhf\n\n hiasdufhuiasdfh";
//        Log.d("debuggg", ster);
//        FileHelper.getInstance().writeToFile(ster, getContext());
//        ster = FileHelper.getInstance().readFromFile(getContext());
//        Log.d("debuggg", ster);
    }

    private void sendMessage() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Sending Email");
        dialog.setMessage("Please wait");
        dialog.show();
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MailSender sender = new MailSender("//", "//");
                    sender.sendMail("EmailSender App",
                            "This is the message body",
                            "//",
                            "//");
                    dialog.dismiss();
                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
    }
}
