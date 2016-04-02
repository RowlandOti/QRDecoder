package com.rowland.qrdecoder.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rowland.qrdecoder.R;
import com.rowland.qrdecoder.ui.activities.ScanActivity;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ResultFragment extends Fragment {

    public static final String REQ_SCAN_RESULTS = "scanResults";
    public static final int REQ_SCAN_CODE = 1;

    private final String EAN_CONTENT = "eanContent";
    // ButterKnife injected views
    // The surface view containing layout
    @Bind(R.id.scan_button)
    Button mScanButton;
    @Bind(R.id.scan_result_textview)
    TextView mScanResultView;

    public ResultFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mScanResultView != null) {
            outState.putString(EAN_CONTENT, mScanResultView.getText().toString());
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_result, container, false);
        // Inflate all views
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mScanResultView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //no need
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //no need
            }

            @Override
            public void afterTextChanged(Editable s) {
                String ean = s.toString();
                //catch isbn10 numbers
                if (ean.length() == 10 && !ean.startsWith("978")) {
                    ean = "978" + ean;
                }
                if (ean.length() < 13) {
                    clearFields();
                    return;
                }
            }
        });

        mScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent object
                Intent scanIntent = new Intent(getActivity(), ScanActivity.class);
                startActivityForResult(scanIntent, REQ_SCAN_CODE);
            }
        });

        if (savedInstanceState != null) {
            mScanResultView.setText(savedInstanceState.getString(EAN_CONTENT));
            mScanResultView.setHint("");
        }
    }

    private void clearFields() {
        mScanResultView.setText("");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity.setTitle(R.string.scan);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {

        super.onActivityResult(requestCode, resultCode, dataIntent);

        // The sign up activity returned that the user has successfully created an account
        if (requestCode == ResultFragment.REQ_SCAN_CODE && resultCode == getActivity().RESULT_OK) {
            String scanResult = dataIntent.getStringExtra(REQ_SCAN_RESULTS);
            mScanResultView.setText(scanResult);
        }
    }
}
