//package itcast.lib_dsgl;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class DsglActivityMainActivity extends Activity {
//
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dsgl_activity_main);
//       final DragSortGridLayout showDsgl = (DragSortGridLayout) findViewById(R.id.dsgl_main_show_dsgl);
//        final  DragSortGridLayout hideDsgl = (DragSortGridLayout) findViewById(R.id.dsgl_main_hide_dsgl);
//
//        List<String> showItems = new ArrayList<>();
//        showItems.add("柳岩");
//        showItems.add("曾辉");
//        showItems.add("范冰冰");
//        showItems.add("李冰冰");
//        showItems.add("饭岛爱");
//        showItems.add("刘涛");
//        showItems.add("高圆圆");
//        showItems.add("杨幂");
//        showItems.add("杨颖");
//        showItems.add("刘诗诗");
//        showItems.add("李宇春");
//        showDsgl.setItems(showItems);
//        showDsgl.setAllowDrag(true);
//        showDsgl.setOnItemClickListener(new DragSortGridLayout.OnItemClickListener(){
//            @Override
//            public void onItemClick(View view,String text) {
//                showDsgl.removeView(view);
////                hideDsgl.addView(view);
//                hideDsgl.addItem(text);
//            }
//        });
//
//
//
//
//        List<String> hideItems = new ArrayList<>();
//        hideItems.add("刘德华");
//        hideItems.add("周润发");
//        hideItems.add("TFBoy");
//        hideItems.add("吴奇隆");
//        hideItems.add("洪波");
//        hideItems.add("陈冠希");
//        hideItems.add("宋仲基");
//        hideItems.add("李敏镐");
//        hideItems.add("都敏俊熙");
//        hideItems.add("赵忠祥");
//        hideItems.add("赵本山");
//        hideItems.add("小沈阳");
//        hideItems.add("李玉刚");
//
//        hideDsgl.setItems(hideItems);
//        hideDsgl.setAllowDrag(false);
//
//        hideDsgl.setOnItemClickListener(new DragSortGridLayout.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view,String text) {
//                hideDsgl.removeView(view);
////                showDsgl.addView(view);
//                showDsgl.addItem(text);
//            }
//        });
//    }
//
//}
