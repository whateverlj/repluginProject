package com.example.myplugindemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ThreeFragment extends Fragment {
    private View rootView;//缓存Fragment view
    RVLayoutManager stackLayoutManager;
    RecyclerView mRecyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_three, container, false);
        } else {
            //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        }
        mRecyclerView = (rootView).findViewById(R.id.recycler);
//         new StackLayoutManager(getActivity(),-56); //堆叠模式
        mRecyclerView.setLayoutManager(stackLayoutManager = new RVLayoutManager());

        mRecyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_stack_card,
                        viewGroup, false);

                BaseViewHolder holder = new BaseViewHolder(view);
                return holder;
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
                ImageView img =((ImageView) viewHolder.itemView.findViewById(R.id.img));
                if(i == 0){
                    img.setBackgroundResource(R.drawable.img1);
                }else if(i == 1){
                    img.setBackgroundResource(R.drawable.img2);
                }else if(i == 2){
                    img.setBackgroundResource(R.drawable.img3);
                }else if(i == 3){
                    img.setBackgroundResource(R.drawable.img4);
                }else if(i == 4){
                    img.setBackgroundResource(R.drawable.img5);
                }
            }

            @Override
            public int getItemViewType(int position) {
                return 1;
            }

            @Override
            public int getItemCount() {
                return 5;
            }
        });

        return rootView;
    }

        class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
