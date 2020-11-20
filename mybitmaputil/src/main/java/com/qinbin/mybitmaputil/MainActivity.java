package com.qinbin.mybitmaputil;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {


    private MyBitmapUtil mMyBitmapUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.lv);
        listView.setAdapter(mBaseAdapter);
        mMyBitmapUtil = new MyBitmapUtil(this);
    }

    private BaseAdapter mBaseAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CommonViewHolder cvh = CommonViewHolder.createCVH(convertView, R.layout.item, parent);
            mMyBitmapUtil.display(cvh.getIv(R.id.item_iv), list.get(position));

            return cvh.convertView;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
    };

    List<String> list = new ArrayList<>();

    {

        list.add("http://ym.zdmimg.com/201509/08/55ee8ba231eb5.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee9c5c41a7c1420.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee87f4a92e8.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee96852b351.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee9aece0a8d.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee7c2110f12.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee7c0a21d4b.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee87a3d180e.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee91be9d6f1.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee98c5825036013.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee9936cc58d1475.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee7c426517a.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee94e87bdd75719.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee93e07603c2316.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee866fd49e7.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee932392cdc8792.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee937cce6f6622.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee92b971f3c2272.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee91c3a890c5538.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee9146d5d2b5060.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee8d42bf4aa.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eea03e15dd3.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee8ba73d873.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee8634b6972.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee8a67e9992.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eea457dd5693052.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee9c70c4fc92262.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eea2d11f3c53202.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eea250a8f2c8031.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee88c9e2c76.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee996f837834313.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eea1d6ab88d133.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee9de545d44907.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee98070351d.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee7f2f60e87.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eea0f2689b28273.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eea551018104929.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee7e4fd37ef.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee84fe7bae1.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee9daa4c7624200.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eeb92aa198b.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee9eaf9c6aa.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eeb17b5530d.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eeb1df21c972378.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee975725611.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee8a04cc706.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eeab36932ad5689.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/02/55e6e7520c3ae.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee9ad536ca4.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eea92e7fca27576.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eeac17d97a94050.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eea2ede8fbd.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eea638e64fc7135.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eea579e701f.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eea3992a48f2235.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eeaa4da9d3c6655.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee8e04aabf1.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee9a097492f.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eea0678ae44.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eea58f3cbb23749.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eeab4936ff9.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eeb1a08dbc5.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eea820a046b.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eed58996d9a.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eed339177be.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eea2147dd35.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee9f96ab830.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee99135aa38.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee957da4931.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eec71f0ec2c.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eecd220e375.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eea6a376dae.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee8dee3634f.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eeccf48cd05554.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee87baee15c.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eebf3945a967120.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eebfc09cdf7.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eec347045023508.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee7f45bf95a.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55eebe606f33f5785.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee774fb74396631.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee5a66a0085.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee7899600a34093.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee77340f62a6023.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee6337431c7.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee681e72cf7.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee68d19b580.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee76f96dcbe1795.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee77517e7aa4763.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee690da1cb7.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee76b2b67402598.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee56ee736a2.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee6b250c263.jpg_d200.jpg");
        list.add("http://y.zdmimg.com/201509/08/55ee754a22211804.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee5ef7d1379.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee5c9a98eb9.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee75cf414888161.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee56c049c0d.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee5c3f9cf24.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee5c6f3ae3a.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee7e21a752e7273.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee608270a1f.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee79a41818e.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee7a4e07f3f.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee79125c592.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee7b2fcc3bf3950.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee706f4b313.jpg_d200.jpg");
        list.add("http://y.zdmimg.com/201509/08/55ee7b2c047af3821.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee7c6f27e7d8640.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee92b669e756330.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee7dec5b0ec7524.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee6b0a1d711.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee7b03b9278.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee6132bbf40.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee78175224b.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee6349d301d.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee6e4fc6ea3.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee79a31bf0850.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee785e28145.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee79c432a109259.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee6f00a08d2.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee6a4faa013.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee81d96cbad8062.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee7a83b30cd.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee6f65a5951.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee75496f62d.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee62c0b5169.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee5a9d72e12.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee77a18f2fe.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee77a873aef.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee7f6e312ef8246.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee6530bdd9d.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee7f7b543b8103.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee714cd41dc.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee7ee306868.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee5168708fa.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee6458261f4.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee7ecf72728186.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee59fc06b84.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee7dcb852d28792.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee69a657606.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee7a5422a7b.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee87dfde4308497.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee7b363e861.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee7fd328dde.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee87457f3699073.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee86a26cd104681.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee7e4e72938.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee7f969e74e.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee84d70a822779.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee84c3d20604695.png_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee65e902d6e.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee817a83b8b.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee83b3e26902687.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/05/55ea48ec3d917.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee5fa77fd15.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee750fc70f1.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee700df0aed.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/07/55ed92b1c5f7d.jpg_d200.jpg");
        list.add("http://ym.zdmimg.com/201509/08/55ee6d69d4602.jpg_d200.jpg");
    }
}
