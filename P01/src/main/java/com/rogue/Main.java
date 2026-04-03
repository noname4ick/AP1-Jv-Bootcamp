package com.rogue;

import com.rogue.data.save.SaveService;
import com.rogue.presentation.presenter.GamePresenter;
import com.rogue.presentation.view.LanternaGameView;

public class Main {

    public static void main(String[] args){

        SaveService saveService =
                new SaveService();

        GamePresenter presenter =
                new GamePresenter(saveService);

        LanternaGameView view =
                new LanternaGameView(presenter);

        presenter.setView(view);

        view.start();}
    }
