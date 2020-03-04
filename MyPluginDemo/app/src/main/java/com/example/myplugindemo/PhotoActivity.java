package com.example.myplugindemo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class PhotoActivity extends Activity implements View.OnClickListener {

    private EditText mETphotoName;
    private EditText mETmodel;
    private ImageView mImage;

    private Uri mPhotoUri;           //拍照后的uri
    public static final int REQUEST_CODE_TAKE_PICTURE = 3;         //拍照显示图片

    private String mPhotoName;
    private String mModel;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setUpViews();
    }

    private void setUpViews(){
        setContentView(R.layout.activity_main1);
        mETphotoName = (EditText)findViewById(R.id.et_photoname);
        mETmodel = (EditText)findViewById(R.id.et_model);
        mImage = (ImageView)findViewById(R.id.image);
    }

    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_getphoto:
                mPhotoName = mETphotoName.getEditableText().toString();
                mModel = mETmodel.getEditableText().toString();
//                if(TextUtils.isEmpty(mPhotoName) || TextUtils.isEmpty(mModel)){
//                    Toast.makeText(this,"请输入图片名称和型号",0).show();
//                    return;
//                }
                final String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    final Intent takePictureImIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    ContentValues values = new ContentValues();
                    mPhotoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    takePictureImIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                    startActivityForResult(takePictureImIntent,REQUEST_CODE_TAKE_PICTURE);
                } else {
//                    Toast.makeText(PhotoActivity.this,"请检查内存卡", 3000).show();
                }
                break;

            default:
                break;
        }

    }


    /* (non-Javadoc)
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case REQUEST_CODE_TAKE_PICTURE:
                // 通过照相获取图片
                if (resultCode == Activity.RESULT_OK) {
                    final Uri uri = mPhotoUri;
                    Log.e("MainActivity",uri+"");
                    if (uri != null) {
                        processPicture(uri);
                    }

                    //提示保存图片
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("提示");
                    builder.setMessage("是否需要保存图片？");
                    builder.setNegativeButton("取消", null);
                    builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String resetPhotoName = mPhotoName+"_"+mModel+"_"+Utils.getRandomString(4);
                            Utils.savePhotoToSDCard(bitmap,Utils.genProjectPath(),resetPhotoName);
//                            Toast.makeText(PhotoActivity.this,"成功--->"+Utils.genProjectPath()+resetPhotoName,0).show();

                            //清空文本框
                            mETphotoName.setText("");
                            mETmodel.setText("");

                            dialog.cancel();
                        }
                    });
                    builder.show();

                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


    /**
     * 图片显示
     * @param uri
     */
    private void processPicture(Uri uri) {
        final String[] projection = {MediaStore.Images.Media.DATA};
        final Cursor cursor = managedQuery(uri, projection, null, null, null);
        cursor.moveToFirst();
        final int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        String imagePath = cursor.getString(columnIndex);
        Log.e("MainActivity",imagePath);

        final String targetPath = BitmapUtils.toRegularHashCode(imagePath) + ".jpg";
        BitmapUtils.compressBitmap(imagePath, targetPath, 640);  //压缩
        bitmap = BitmapUtils.decodeBitmap(imagePath, 150);       //分解
        mImage.setImageBitmap(bitmap);                           //显示

        //上传服务器
        //final AvatarUrlUpdateRequest request = new AvatarUrlUpdateRequest(REQUEST_ID_AVATAR_URL_UPDATE,mFileName,targetPath,mLoginResult);
        //final LoginRequest request = new LoginRequest(0,"a@a.com","123456");
        //genericDataManager.retrieveData(request, this);

    }


}
