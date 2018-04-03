package org.bitman.ay27.view.sign_in_up;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.andreabaccega.widget.FormEditText;
import org.bitman.ay27.R;
import org.bitman.ay27.common.ToastUtils;
import org.bitman.ay27.module.dp.UserDP;
import org.bitman.ay27.view.main.MainActivity;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-7-25.
 */
public class SignUpPage extends Fragment {

    @InjectView(R.id.sign_up_user_name)
    FormEditText usernameEdit;
    @InjectView(R.id.sign_up_user_password)
    FormEditText passwordEdit;
    @InjectView(R.id.sign_up_user_password_confirm)
    FormEditText passwordConfirmEdit;
    @InjectView(R.id.sign_up_user_email)
    FormEditText emailEdit;
    @InjectView(R.id.sign_up_confirm)
    Button confirmBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_fragment, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.sign_up_confirm)
    public void confirm(View view) {

        boolean flag = usernameEdit.testValidity() && passwordConfirmEdit.testValidity() && passwordConfirmEdit.testValidity() && emailEdit.testValidity();
        if (!flag)
            return;

        String username = getUsername();
        String password = getPassword();
        String password2 = getPasswordConfirm();
        String email = getEmail();

        if (!password.equals(password2)) {
            ToastUtils.showError(getActivity(), R.string.sign_up_password_unequal);
            return;
        }

        SignUtils.SignCallback callback = new SignUtils.SignCallback() {
            @Override
            public void onSuccess(UserDP userDP) {
                ToastUtils.showSuccess(getActivity(), R.string.sign_up_success);
                start2MainActivity();
            }

            @Override
            public void onFailed() {
                ToastUtils.showError(getActivity(), R.string.sign_up_failed);
            }
        };
        ToastUtils.showWaiting(getActivity(), getString(R.string.sign_up_processing));
        SignUtils.signUp(username, password, email, callback);
    }

    private void start2MainActivity() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }


    private String getUsername() {
        return usernameEdit.getText().toString();
    }

    private String getPassword() {
        return passwordEdit.getText().toString();
    }

    private String getPasswordConfirm() {
        return passwordConfirmEdit.getText().toString();
    }

    private String getEmail() {
        return emailEdit.getText().toString();
    }

}
