package com.planning.college.collegeplanning.elicitation.selfArticle;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

import com.planning.college.collegeplanning.R;
import com.planning.college.fragment.elicitation.EditArticleFragment;
import com.planning.college.fragment.elicitation.EditArticleFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

@EActivity(R.layout.article_edit)
public class EditArticalActivity extends Activity {

    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Extra("tag")
    String tag;

    @Extra("a_no")
    String a_no;

    @Extra("indexPath")
    String indexPath;



    @AfterViews
    public void afterViews(){
        manager = getFragmentManager();
        transaction = manager.beginTransaction();

        EditArticleFragment_ editArticleFragment = new EditArticleFragment_();
        Bundle bundle = new Bundle();
        bundle.putString("tag",tag);

        Toast.makeText(this,"文章编号:"+a_no,Toast.LENGTH_SHORT).show();
        bundle.putString("a_no",a_no);
        bundle.putString("indexPath",indexPath);

        editArticleFragment.setArguments(bundle);
        transaction.add(R.id.container,editArticleFragment);
        transaction.commit();


    }


}