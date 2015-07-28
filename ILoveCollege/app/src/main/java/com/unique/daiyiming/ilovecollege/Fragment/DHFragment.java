package com.unique.daiyiming.ilovecollege.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.unique.daiyiming.ilovecollege.Adapter.TopicListAdapter;
import com.unique.daiyiming.ilovecollege.Config.Config;
import com.unique.daiyiming.ilovecollege.MySharedPreference.LocalUserInfoSharedPreference;
import com.unique.daiyiming.ilovecollege.NetServiceTools.JsonPostHelper;
import com.unique.daiyiming.ilovecollege.R;
import com.unique.daiyiming.ilovecollege.View.PullDownFlushListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by daiyiming on 2015/6/19.
 * 大伙
 */
public class DHFragment extends Fragment implements PullDownFlushListView.OnPullDownFlushListViewFlushListener, TopicListAdapter.OnTopicListControlButtonClickListener {

    private final static String SEND_URL_TOPICS = Config.BASE_URL + "/forum/index/"; //获取topics网址
    private final static String SEND_URL_LIKE = Config.BASE_URL + "/forum/favorite/"; //获取topics网址

    private final static String SEND_PARAMS_USERNAME = "username";
    private final static String SEND_PARAMS_TOKEN = "token";
    private final static String SEND_PARAMS_NODE_ID = "node_id";
    private final static String SEND_PARAMS_PAGE = "page";
    private final static String SEND_PARAMS_TOPIC_ID = "topic_id";
    private final static String NODE_ID_DH = "1"; //大伙发送此

    private final static String GET_PARAMS_TOKEN = "token"; //token
    private final static String GET_PARAMS_TOPICS = "topics"; //token


    private PullDownFlushListView pdflv_listView = null;
    private TopicListAdapter adapter = null;
    private LocalUserInfoSharedPreference userInfoSharedPreference = null;
    private JsonPostHelper jsonPostHelper = null;
    private int currentPage = 1; //当前Topic列表所处的页数

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dh, null);

        pdflv_listView = (PullDownFlushListView) view.findViewById(R.id.pdflv_listView);
        adapter = new TopicListAdapter(getActivity());
        pdflv_listView.setAdapter(adapter);
        pdflv_listView.setOnPullDownFlushListViewFlushListener(this);

        userInfoSharedPreference = new LocalUserInfoSharedPreference(getActivity());
        jsonPostHelper = new JsonPostHelper(getActivity());

        adapter.setOnTopicListControlButtonClickListener(this);

//        for (int i = 0; i < 10; i ++) {
//            Map<String, String> map = new HashMap<>();
//            map.put(TopicListAdapter.MAP_KEY_ID, i + "");
//            map.put(TopicListAdapter.MAP_KEY_IMAGE_COUNT, i + "");
//            map.put(TopicListAdapter.MAP_KEY_IMAGE, "");
//            map.put(TopicListAdapter.MAP_KEY_CONTENT, "" + i);
//            map.put(TopicListAdapter.MAP_KEY_GENDER, (i % 2) + "");
//            map.put(TopicListAdapter.MAP_KEY_LIKE_COUNT, i + "");
//            map.put(TopicListAdapter.MAP_KEY_REPLY_COUNT, i + "");
//            if (i == 2 || i == 6) {
//                map.put(TopicListAdapter.MAP_KEY_VOICE, "123");
//            } else {
//                map.put(TopicListAdapter.MAP_KEY_VOICE, "");
//            }
//            if (i != 0) {
//                map.put(TopicListAdapter.MAP_KEY_IMAGE, "http://192.168.56.1/test/" + i + ".png");
//            }
//
//            if (i == 2 || i == 6 || i == 8) {
//                map.put(TopicListAdapter.MAP_KEY_IS_LIKED, "1");
//            } else {
//                map.put(TopicListAdapter.MAP_KEY_IS_LIKED, "0");
//            }
//            adapter.addItem(map);
//        }
//        adapter.notifyDataSetInvalidated();

        OnPullDownFlushListViewFlush();

        return view;
    }


    @Override
    public void OnPullDownFlushListViewFlush() {
        pdflv_listView.lockFlush(); //锁定刷新
        currentPage = 1; //限定为第一页
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(SEND_PARAMS_USERNAME, userInfoSharedPreference.getUsername());
            jsonObject.put(SEND_PARAMS_TOKEN, userInfoSharedPreference.getToken());
            jsonObject.put(SEND_PARAMS_NODE_ID, NODE_ID_DH);
            jsonObject.put(SEND_PARAMS_PAGE, currentPage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonPostHelper.getJsonResponse(SEND_URL_TOPICS, jsonObject, new JsonPostHelper.OnJsonPostHelperResonsedListener() {
            @Override
            public void OnJsonPostHelperResponsed() {
                pdflv_listView.unLockFlush(); //解锁刷新
            }
        }, new JsonPostHelper.OnJsonPostHelperResonseSuccessedListener() {
            @Override
            public void OnJsonPostHelperResonseSuccessed(JSONObject response) {
                try {
                    adapter.clearAllItem();
                    JSONArray topics = response.getJSONArray(GET_PARAMS_TOPICS);
                    for (int i = 0; i < topics.length(); i++) {
                        JSONObject topic = topics.getJSONObject(i);
                        Map<String, String> item = new HashMap<>();
                        item.put(TopicListAdapter.MAP_KEY_ID, topic.getString(TopicListAdapter.MAP_KEY_ID));
                        item.put(TopicListAdapter.MAP_KEY_GENDER, topic.getString(TopicListAdapter.MAP_KEY_GENDER));
                        item.put(TopicListAdapter.MAP_KEY_CONTENT, topic.getString(TopicListAdapter.MAP_KEY_CONTENT));
                        item.put(TopicListAdapter.MAP_KEY_VOICE, topic.getString(TopicListAdapter.MAP_KEY_VOICE));
                        item.put(TopicListAdapter.MAP_KEY_IMAGE_COUNT, topic.getString(TopicListAdapter.MAP_KEY_IMAGE_COUNT));
                        item.put(TopicListAdapter.MAP_KEY_IMAGE, topic.getString(TopicListAdapter.MAP_KEY_IMAGE));
                        item.put(TopicListAdapter.MAP_KEY_LIKE_COUNT, topic.getString(TopicListAdapter.MAP_KEY_LIKE_COUNT));
                        item.put(TopicListAdapter.MAP_KEY_REPLY_COUNT, topic.getString(TopicListAdapter.MAP_KEY_REPLY_COUNT));
                        item.put(TopicListAdapter.MAP_KEY_IS_LIKED, topic.getString(TopicListAdapter.MAP_KEY_IS_LIKED));
                        adapter.addItem(item);
                    }
                    pdflv_listView.setLastFlushTime(System.currentTimeMillis()); //重置上次刷新时间
                    adapter.notifyDataSetInvalidated();
                    pdflv_listView.showSuccessWaitView();
                    pdflv_listView.showFootView(); //数据添加成功显示尾部控件
                    //TODO
//                    userInfoSharedPreference.putToken(response.getString(GET_PARAMS_TOKEN));
//                    userInfoSharedPreference.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new JsonPostHelper.OnJsonPostHelperResonseFiledListener() {
            @Override
            public void OnJsonPostHelperResonseFiled(String cause) {
                pdflv_listView.showFailWaieView();
                pdflv_listView.hideFootView(); //数据添加失败隐藏尾部控件
            }
        });
    }

    @Override
    public void OnPullDownFlushListViewLoadMore() {
    }

    @Override
    public void OnLikeButtonClickedListener(final int position, int isLiked, String topicId) {
        if (isLiked == TopicListAdapter.IS_LIKE_TRUE) {
            Toast.makeText(getActivity(), "不能重复点赞哦~", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setItemLiked(position);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(SEND_PARAMS_USERNAME, userInfoSharedPreference.getUsername());
                jsonObject.put(SEND_PARAMS_TOKEN, userInfoSharedPreference.getToken());
                jsonObject.put(SEND_PARAMS_TOPIC_ID, topicId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //发送数据
            jsonPostHelper.getJsonResponse(SEND_URL_TOPICS, jsonObject, null, new JsonPostHelper.OnJsonPostHelperResonseSuccessedListener() {
                @Override
                public void OnJsonPostHelperResonseSuccessed(JSONObject response) {
//              TODO
//                    try {
//                        userInfoSharedPreference.putToken(response.getString(GET_PARAMS_TOKEN));
//                        userInfoSharedPreference.commit();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            }, new JsonPostHelper.OnJsonPostHelperResonseFiledListener() {
                @Override
                public void OnJsonPostHelperResonseFiled(String cause) {
                    Toast.makeText(getActivity(), "点赞失败" + (cause.equals("") ? cause : " " + cause), Toast.LENGTH_SHORT).show();
                    adapter.setItemUnLiked(position);
                }
            });
        }
    }

    @Override
    public void OnCommentButtonClickedListener(int positon) {

    }
}
