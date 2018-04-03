package org.bitman.ay27.view.writer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import org.bitman.ay27.PickerWidget.BookPageIndicator;
import org.bitman.ay27.R;
import org.bitman.ay27.common.ContentType;
import org.bitman.ay27.common.ImageDecodeUtils;
import org.bitman.ay27.common.ToastUtils;
import org.bitman.ay27.common.Utils;
import org.bitman.ay27.view.writer.crop.CropActivity;
import org.bitman.ay27.view.writer.crop.CutImageActivity;
import org.bitman.ay27.view.writer.editor_button.EditorButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ay27 on 14/11/17.
 */
public class WriteContentFragment extends Fragment {

    private static final int GET_PICTURE = 0x46;
    private static final int TAKE_PHOTO = 0x256;
    private static final int RESULT_OK = Activity.RESULT_OK;
    private static final int ACTION_CROP_IMAGE = 0x15;
    private static final int ACTION_CUT_IMAGE = 0x73;

    @InjectView(R.id.write_content_indicator)
    BookPageIndicator indicator;

    @InjectView(R.id.write_content_link_button)
    Button linkButton;
    @InjectView(R.id.write_content_pic_button)
    Button picButton;

    @InjectViews({R.id.write_content_button1, R.id.write_content_button2, R.id.write_content_button3, R.id.write_content_button4, R.id.write_content_button5, R.id.write_content_button6, R.id.write_content_button7})
    List<EditorButton> editorButtons;

    @InjectView(R.id.bottom_layout)
    View bottomLayout;
    @InjectView(R.id.bottom_layout_md)
    View bottomLayoutMd;

    @InjectView(R.id.write_content_header)
    EditText header;
    @InjectView(R.id.write_content_answer_question_title)
    TextView questionTitle;
    @InjectView(R.id.write_content_content)
    EditText content;


    private long chooseBookId, questionId;
    private String questionTitleStr;
    private ContentType type;
    private String chooseBookName;
    private int chooseBookPage;
    private ArrayList<Uri> uploadFiles;
    private Uri photoUri;


    public WriteContentFragment(ContentType type, long chooseBookId, String chooseBookName, int chooseBookPage, ArrayList<Uri> uploadFiles) {
        this.chooseBookId = chooseBookId;
        this.type = type;
        this.chooseBookName = chooseBookName;
        this.chooseBookPage = chooseBookPage;
        this.uploadFiles = uploadFiles;
    }

    public WriteContentFragment(ContentType type, String chooseBookName, int chooseBookPage, long questionId, String questionTitle) {
        this.type = type;
        this.chooseBookName = chooseBookName;
        this.chooseBookPage = chooseBookPage;
        this.questionTitleStr = questionTitle;
        this.questionId = questionId;
    }

    @OnClick(R.id.write_content_link_button)
    public void linkBtnClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.write_link_dialog, null);
        final EditText first = (EditText) view.findViewById(R.id.write_dialog_first);
        final EditText second = (EditText) view.findViewById(R.id.write_dialog_second);

        builder.setTitle(getString(R.string.write_link_input_title));
        builder.setView(view);
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String a = first.getText().toString().trim();
                String b = second.getText().toString().trim();

                if (b == null || b.equals("")) {
                    ToastUtils.showError(getActivity(), getString(R.string.add_link_failed));
                    return;
                }

                insertLink(a, b);
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), null);
        builder.show();
    }

    @OnClick(R.id.write_content_pic_button)
    public void picBtnClick(View v) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.choose))
                .setItems(getResources().getStringArray(R.array.picture_source), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0)
                            chooseImage();
                        else
                            takePhoto();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.write_write_content, null);
        ButterKnife.inject(this, view);

        initViews();

        setHasOptionsMenu(true);

        return view;
    }

    private void initViews() {
        for (final EditorButton button : editorButtons) {
            button.setEditor(content);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    button.processClick();
                }
            });
        }

        if (type != ContentType.Answer) {
            questionTitle.setVisibility(View.GONE);
        } else {
            header.setVisibility(View.GONE);
            content.setHint(getActivity().getString(R.string.answer_question_content_hint));
            questionTitle.setText(questionTitleStr);
        }

        indicator.setText(chooseBookName, chooseBookPage);

        setUpBottomMenu();


    }

    private void setUpBottomMenu() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int chooseItem = preferences.getInt("editor", -1);
        if (chooseItem == 0) {
            bottomLayout.setVisibility(View.VISIBLE);
            bottomLayoutMd.setVisibility(View.GONE);
        }
        else if (chooseItem == 1) {
            bottomLayoutMd.setVisibility(View.VISIBLE);
            bottomLayout.setVisibility(View.GONE);
        }

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, GET_PICTURE);
    }

    private void takePhoto() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        String cacheDir = Utils.getDiskCacheDir(getActivity());
        File file = new File(cacheDir, "" + System.currentTimeMillis() + ".jpg");
        photoUri = Uri.fromFile(file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }


    private void insertLink(String a, String b) {
        SpannableString ss;
        String str = (a == null || a.equals("")) ? b : a;
        ss = new SpannableString(str);
        ss.setSpan(new URLSpan(b), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        content.append(ss);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == GET_PICTURE && resultCode == RESULT_OK) {
                photoUri = data.getData();
                showCropImageDialog(photoUri);

            } else if (requestCode == TAKE_PHOTO && resultCode == RESULT_OK) {
                showCropImageDialog(photoUri);

            } else if (resultCode == RESULT_OK && (requestCode == ACTION_CROP_IMAGE || requestCode == ACTION_CUT_IMAGE)) {
                insertPic(ImageDecodeUtils.decodeSource(getActivity(), data.getData(), 300), data.getData());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    //    <string-array name="crop_image_dialog">
//    <item>裁剪</item>
//    <item>纠偏+裁剪</item>
//    <item>直接使用</item>
//    </string-array>
    private void showCropImageDialog(final Uri photoUri) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(R.array.crop_image_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setData(photoUri);
                int requestCode = -1;

                try {
                    switch (which) {
                        case 0:
                            intent.setClass(getActivity(), CropActivity.class);
                            requestCode = ACTION_CROP_IMAGE;
                            break;
                        case 1:
                            intent.setClass(getActivity(), CutImageActivity.class);
                            requestCode = ACTION_CUT_IMAGE;
                            break;
                        case 2:
                            insertPic(ImageDecodeUtils.decodeSource(getActivity(), photoUri, 300), photoUri);
                            return;
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                startActivityForResult(intent, requestCode);

            }
        });
        builder.show();
    }

    private void insertPic(Bitmap bitmap, Uri picUri) {
        ImageSpan imageSpan = new ImageSpan(getActivity(), bitmap, ImageSpan.ALIGN_BASELINE);
        String str = "<image>" + picUri.toString() + "</image>";
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(imageSpan, 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        content.append("\n");
        content.append(spannableString);
        content.append("\n");
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.write, menu);
        menu.getItem(0).setTitle(getActivity().getString(R.string.write_content_finish));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.write_commit) {
            start2ShowActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void start2ShowActivity() {
        Intent intent = new Intent(getActivity(), ShowActivity.class);
        if (header.getVisibility() == View.VISIBLE && Utils.isNull(header.getText().toString())) {
            ToastUtils.showError(getActivity(), getString(R.string.write_acquire_title));
            return;
        }
        if (header.getVisibility() == View.VISIBLE && !Utils.isNull(header.getText().toString())) {
            intent.putExtra("Header", header.getText().toString());
        }
        if (Utils.isNull(content.getText().toString())) {
            ToastUtils.showError(getActivity(), getString(R.string.write_acquire_content));
            return;
        }
        intent.putExtra("Content", WriteParser.text2Show(getActivity(), content.getText()));

        intent.putExtra("BookPage", chooseBookPage);
        intent.putExtra("BookName", chooseBookName);
        intent.putExtra("BookID", chooseBookId);
        intent.putExtra("QuestionID", questionId);

        if (uploadFiles != null) {
            intent.putParcelableArrayListExtra("AttachmentFileList", uploadFiles);
        }

        intent.putExtra("Type", type);
        startActivity(intent);
    }
}
