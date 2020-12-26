package edu.hubu.yousee;

import android.app.Fragment;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link youseeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class youseeFragment extends Fragment implements View.OnClickListener {
    private View view;
    private MyDAO myDAO;  //数据库访问对象
    private ListView listView;
    private List<Map<String,Object>> listData;
    private Map<String,Object> listItem;
    private SimpleAdapter listAdapter;

    private EditText et_name;  //数据表包含3个字段，第1字段为自增长类型
    private EditText et_price;

    private  String selId=null;  //选择项id
    Button button1,button2,button3;
    ImageView imageView;


//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    public youseeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment youseeFragment.
     */
//    // TODO: Rename and change types and number of parameters
//    public static youseeFragment newInstance(String param1, String param2) {
//        youseeFragment fragment = new youseeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.demo01, container, false);
        Button bt_add= (Button) view.findViewById(R.id.bt_add);bt_add.setOnClickListener(this);
        Button bt_modify=(Button) view.findViewById(R.id.bt_modify);bt_modify.setOnClickListener(this);
        Button bt_del=(Button) view.findViewById(R.id.bt_del);bt_del.setOnClickListener(this);

        button1=view.findViewById(R.id.bt_click1);
        button2=view.findViewById(R.id.bt_click2);
        button3=view.findViewById(R.id.bt_click3);
        imageView=view.findViewById(R.id.imageView);

        et_name=(EditText) view.findViewById(R.id.et_name);
        et_price=(EditText) view.findViewById(R.id.et_price);

        myDAO = new MyDAO(getActivity());  //创建数据库访问对象
        if(myDAO.getRecordsNumber()==0) {  //防止重复运行时重复插入记录
            myDAO.insertInfo("tian", 20);   //插入记录
            myDAO.insertInfo("wang", 40); //插入记录
        }

        displayRecords();   //显示记录

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testGet1();

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testPost();

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testpng();
            }
        });

        return view;
    }

    public void displayRecords(){  //显示记录方法定义
        listView = (ListView)view.findViewById(R.id.listView);
        listData = new ArrayList<Map<String,Object>>();
        Cursor cursor = myDAO.allQuery();
        while (cursor.moveToNext()){
            int id=cursor.getInt(0);  //获取字段值
            String name=cursor.getString(1);
            //int age=cursor.getInt(2);
            int price=cursor.getInt(cursor.getColumnIndex("price"));//推荐此种方式
            listItem=new HashMap<String,Object>(); //必须在循环体里新建
            listItem.put("_id", id);  //第1参数为键名，第2参数为键值
            listItem.put("name", name);
            listItem.put("price", price);
            listData.add(listItem);   //添加一条记录
        }
        listAdapter = new SimpleAdapter(getActivity(),
                listData,
                R.layout.item_layout, //自行创建的列表项布局
                new String[]{"_id","name","price"},
                new int[]{R.id.tv_id,R.id.tvname,R.id.tvprice});
        listView.setAdapter(listAdapter);  //应用适配器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {  //列表项监听
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String,Object> rec= (Map<String, Object>) listAdapter.getItem(position);  //从适配器取记录
                et_name.setText(rec.get("name").toString());  //刷新文本框
                et_price.setText(rec.get("price").toString());
                Log.i("ly",rec.get("_id").toString());
                selId=rec.get("_id").toString();  //供修改和删除时使用
            }
        });
    }
    @Override
    public void onClick(View v) {
        //实现的接口方法
        if(selId!=null) {  //选择了列表项后，可以增加/删除/修改
            String p1 = et_name.getText().toString().trim();
            int p2 = Integer.parseInt(et_price.getText().toString());
            switch (v.getId()){
                case  R.id.bt_add:
                    myDAO.insertInfo(p1,p2);
                    break;
                case  R.id.bt_modify:
                    myDAO.updateInfo(p1,p2,selId);
                    Toast.makeText(getActivity(),"更新成功！",Toast.LENGTH_SHORT).show();
                    break;
                case  R.id.bt_del:
                    myDAO.deleteInfo(selId);
                    Toast.makeText(getActivity(),"删除成功！",Toast.LENGTH_SHORT).show();
                    et_name.setText(null);et_price.setText(null); selId=null; //提示
            }
        }else{  //未选择列表项
            if(v.getId()==R.id.bt_add) {  //单击添加按钮
                String p1 = et_name.getText().toString();
                String p2=et_price.getText().toString();
                if(p1.equals("")||p2.equals("")){  //要求输入了信息
                    Toast.makeText(getActivity(),"name和price都不能空！",Toast.LENGTH_SHORT).show();
                }else{
                    myDAO.insertInfo(p1, Integer.parseInt(p2));  //第2参数转型
                }
            } else{   //单击了修改或删除按钮
                Toast.makeText(getActivity(),"请先选择记录！", Toast.LENGTH_SHORT).show();
            }
        }
        displayRecords();//刷新ListView对象
    }

    public void testGet1(){
        //创建OkHttpClient实例对象
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建Request对象
        Request request = new Request.Builder()
                .url("https://ss1.bdstatic.com/get?id=111")
                .addHeader("key","value")
                .get()
                .build();
        //执行Request请求
        //异步请求
        okHttpClient.newCall(request).enqueue(new Callback() {

            public void onFailure(Call call, IOException e) {
                //请求失败
            }

            public void onResponse(Call call, Response response) throws IOException {
                //请求成功
                Log.d("TestOkHttp",response.body().string());
            }
        });
        //同步请求
        /*
        try {
            Response response = okHttpClient.newCall(request).execute();
            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }


    public void testPost(){
        //1、创建OkHttpClient对象实例
        OkHttpClient okHttpClient = new OkHttpClient();
        //2、创建Request对象
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType,"{}");
        Request request = new Request.Builder()
                .url("https://ss1.bdstatic.com/post")
                .post(requestBody)
                .build();
        //3、执行Request请求
        okHttpClient.newCall(request).enqueue(new Callback() {

            public void onFailure(Call call, IOException e) {
                //请求失败
            }

            public void onResponse(Call call, Response response) throws IOException {
                //请求成功
                Log.d("TestOkHttpPost",response.body().string());
            }
        });

    }

    public void testpng(){
        //1、创建OkHttpClient对象实例
        OkHttpClient okHttpClient = new OkHttpClient();
        //2、创建Request对象
        MediaType mediaType = MediaType.parse("image/jpg; charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType,"{}");
        Request request = new Request.Builder()
                .url("https://wx4.sinaimg.cn/mw690/001SzPzIgy1glzedgyt97j611i1e0qmd02.jpg")
                .get()
                .build();
        //3、执行Request请求
        okHttpClient.newCall(request).enqueue(new Callback() {

            public void onFailure(Call call, IOException e) {
                //请求失败
            }

            public void onResponse(Call call, final Response response) throws IOException {

                final MediaType contenttype = response.body().contentType();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Drawable drawable = Drawable.createFromStream(response.body().byteStream(),"image.png");
                        drawable.setBounds(0,0,1000,1000);
                        imageView.setImageDrawable(drawable);
                    }
                });

                //请求成功

            }
        });

    }

}
