package torrentcome.frinotec;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class ConfigDialogFragment extends DialogFragment {

    public ConfigDialogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_config, container);
        getDialog().setTitle(R.string.config);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Switch show_bar_tempo = (Switch) view.findViewById(R.id.show_bar_tempo);
        EditText set_tempo = (EditText) view.findViewById(R.id.show_tempo);
        Button finish = (Button) view.findViewById(R.id.finish);

        set_tempo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    int number = Integer.parseInt(charSequence.toString());
                    ((MainActivity) getActivity()).setBarView(number);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        show_bar_tempo.setChecked(true);
        show_bar_tempo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ((MainActivity) getActivity()).showBarView(b);
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}