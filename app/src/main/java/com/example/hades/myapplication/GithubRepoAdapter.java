package com.example.hades.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hades on 2017/4/3.
 */

public class GithubRepoAdapter extends BaseAdapter {

    private List<GithubRepo> githubRepos=new ArrayList<>();
    @Override
    public int getCount() {
        return githubRepos.size();
    }

    @Override
    public GithubRepo getItem(int position) {
        if(position<0||position>=githubRepos.size()){
            return null;
        }else{
            return githubRepos.get(position);

        }
    }

    public void setGithubRepos(List<GithubRepo> repos){
        if(repos==null){
            return ;
        }

        githubRepos.clear();
        githubRepos.addAll(repos);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view=(convertView!=null)?convertView:createView(parent);
        final GithubRepoViewHolder viewHolder= (GithubRepoViewHolder) view.getTag();
        viewHolder.setGithubRepo(getItem(position));
        return view;
    }

    private View createView(ViewGroup parent) {
        final LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        final View view=inflater.inflate(R.layout.item_github_repo,parent,false);
        final GithubRepoViewHolder viewHolder=new GithubRepoViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    private static class GithubRepoViewHolder{
        private final TextView textRepoName;
        private final TextView textRepoDescription;
        private final TextView textLanguage;
        private final TextView textStar;

        public GithubRepoViewHolder(View view){
            textRepoName=(TextView)view.findViewById(R.id.text_repo_name);
            textRepoDescription=(TextView)view.findViewById(R.id.text_repo_description);
            textLanguage=(TextView)view.findViewById(R.id.text_language);
            textStar=(TextView)view.findViewById(R.id.text_stars);
        }

        public void setGithubRepo(GithubRepo item) {
            textRepoName.setText(item.name);
            textRepoDescription.setText(item.description);
            textLanguage.setText("Language: "+item.language);
            textStar.setText("Stars: "+item.stargazersCount);
        }
    }


}
