package org.bitman.ay27.view.sign_in_up;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.andreabaccega.widget.FormEditText;
import org.bitman.ay27.R;
import org.bitman.ay27.common.ToastUtils;
import org.bitman.ay27.module.dp.UserDP;
import org.bitman.ay27.request.Urls;
import org.bitman.ay27.view.main.MainActivity;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-7-25.
 */
public class SignInPage extends Fragment {

    private static final String TAG = "Sign In";

    @InjectView(R.id.sign_in_user_name)
    FormEditText usernameEdit;
    @InjectView(R.id.sign_in_user_password)
    FormEditText passwordEdit;
    @InjectView(R.id.sign_in_confirm)
    Button confirmBtn;
//    @InjectView(R.id.sign_ip)
//    EditText ipText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sign_in_fragment, null);
        ButterKnife.inject(this, root);
        return root;
    }

    @OnClick(R.id.sign_in_confirm)
    public void confirm(View view) {

//        Urls.setIp(ipText.getText().toString());

        boolean flag = usernameEdit.testValidity() && passwordEdit.testValidity();
        if (!flag)
            return;

        SignUtils.SignCallback callback = new SignUtils.SignCallback() {
            @Override
            public void onSuccess(UserDP userDP) {
                ToastUtils.showSuccess(getActivity(), R.string.sign_in_success);
                start2MainActivity();
            }

            @Override
            public void onFailed() {
                ToastUtils.showError(getActivity(), R.string.sign_in_failed);
            }
        };
        ToastUtils.showWaiting(getActivity(), getString(R.string.sign_in_processing));
        SignUtils.signIn(getUsername(), getPassword(), callback);
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

}
