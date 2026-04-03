package com.rogue.presentation.presenter;

import com.rogue.data.save.SaveService;
import com.rogue.domain.game.Direction;
import com.rogue.domain.game.GameSession;
import com.rogue.domain.game.GameState;
import com.rogue.domain.items.ItemType;
import com.rogue.presentation.ui.Menu;
import com.rogue.presentation.view.GameView;

public class GamePresenter {

    private GameSession session;

    private GameView view;

    private boolean statsView;

    private final Menu menu;

    public GamePresenter(
            SaveService saveService){

        session =
                new GameSession(
                        saveService);

        // Build the menu; enable Continue only when a save exists
        menu = new Menu(session.hasSave());
    }

    public void setView(GameView view){

        this.view = view;
    }

    // -----------------------------------------------------------------------
    // Menu API
    // -----------------------------------------------------------------------

    public Menu getMenu(){

        return menu;
    }

    public void menuUp(){

        menu.moveUp();

        update();
    }

    public void menuDown(){

        menu.moveDown();

        update();
    }

    public void menuSelect(){

        int selected = menu.getSelected();

        if(selected == Menu.IDX_NEW_GAME){

            session.startNewGame();

        } else if(selected == Menu.IDX_CONTINUE){

            session.continueGame();

        } else if(selected == Menu.IDX_EXIT){

            System.exit(0);
        }

        update();
    }

    // -----------------------------------------------------------------------
    // In-game API (unchanged from original)
    // -----------------------------------------------------------------------

    public boolean isStatsView(){

        return statsView;
    }

    public void toggleStatsView(){

        statsView =
                !statsView;
    }

    public void moveUp(){

        session.movePlayer(
                Direction.UP);

        update();
    }

    public void moveDown(){

        session.movePlayer(
                Direction.DOWN);

        update();
    }

    public void moveLeft(){

        session.movePlayer(
                Direction.LEFT);

        update();
    }

    public void moveRight(){

        session.movePlayer(
                Direction.RIGHT);

        update();
    }

    public void beginWeaponSelection(){

        session.beginSelection(
                ItemType.WEAPON);

        update();
    }

    public void beginFoodSelection(){

        session.beginSelection(
                ItemType.FOOD);

        update();
    }

    public void beginElixirSelection(){

        session.beginSelection(
                ItemType.ELIXIR);

        update();
    }

    public void beginScrollSelection(){

        session.beginSelection(
                ItemType.SCROLL);

        update();
    }

    public void selectSlot(
            int oneToNine){

        session.selectItemSlot(
                oneToNine);

        update();
    }

    public void cancelSelection(){

        session.cancelSelection();

        update();
    }

    public void update(){

        view.render(session);
    }

    public GameSession getSession(){

        return session;
    }

    /**
     * Called when the player presses ESC or a restart key after game-over / victory.
     * Returns the player to the main menu.
     */
    public void returnToMenu(){

        menu.setHasSave(session.hasSave());

        session.setState(GameState.MENU);

        statsView = false;

        update();
    }

}


