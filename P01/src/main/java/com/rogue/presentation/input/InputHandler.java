package com.rogue.presentation.input;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.rogue.domain.game.GameState;
import com.rogue.presentation.presenter.GamePresenter;

import java.io.IOException;

public class InputHandler {

    private final GamePresenter presenter;

    private final Screen screen;

    public InputHandler(
            GamePresenter presenter,
            Screen screen){

        this.presenter =
                presenter;

        this.screen =
                screen;
    }

    /**
     * Called by the game loop when a key has already been read (blocking path used in menu/game-over states).
     */
    public void processKey(KeyStroke key)
            throws IOException{

        if(key == null){

            return;
        }

        dispatch(key);
    }

    /**
     * Called by the game loop in gameplay state — polls for a key without blocking.
     */
    public void process()
            throws IOException{

        KeyStroke key =
                screen.pollInput();

        if(key == null){

            return;
        }

        dispatch(key);
    }

    private void dispatch(KeyStroke key)
            throws IOException{

        if(key.getKeyType() == KeyType.EOF){

            System.exit(0);
        }

        GameState state =
                presenter.getSession().getState();

        // ----------------------------------------------------------------
        // MENU state: only navigate / select menu options
        // ----------------------------------------------------------------
        if(state == GameState.MENU){

            handleMenuInput(key);

            return;
        }

        // ----------------------------------------------------------------
        // GAME_OVER / VICTORY: ESC or Enter returns to menu
        // ----------------------------------------------------------------
        if(state == GameState.GAME_OVER
                || state == GameState.VICTORY){

            if(key.getKeyType() == KeyType.Escape
                    || key.getKeyType() == KeyType.Enter){

                presenter.returnToMenu();
            }

            return;
        }

        // ----------------------------------------------------------------
        // RUNNING state
        // ----------------------------------------------------------------

        // ESC while item selection is active → cancel
        if(presenter.getSession().getPendingSelection() != null){

            if(key.getKeyType() == KeyType.Escape){

                presenter.cancelSelection();

                return;
            }

            if(key.getKeyType() == KeyType.Character){

                char c = key.getCharacter();

                if(c >= '1' && c <= '9'){

                    presenter.selectSlot(c - '0');
                }
            }

            return;
        }

        // ESC in normal gameplay → return to menu
        if(key.getKeyType() == KeyType.Escape){

            presenter.returnToMenu();

            return;
        }

        // Stats view: only 't' closes it
        if(presenter.isStatsView()){

            if(key.getKeyType() == KeyType.Character){

                char c =
                        Character.toLowerCase(
                                key.getCharacter());

                if(c == 't'){

                    presenter.toggleStatsView();
                }
            }

            return;
        }

        // Movement — arrow keys
        if(key.getKeyType() == KeyType.ArrowUp){

            presenter.moveUp();

            return;
        }

        if(key.getKeyType() == KeyType.ArrowDown){

            presenter.moveDown();

            return;
        }

        if(key.getKeyType() == KeyType.ArrowLeft){

            presenter.moveLeft();

            return;
        }

        if(key.getKeyType() == KeyType.ArrowRight){

            presenter.moveRight();

            return;
        }

        // Character keys
        if(key.getKeyType() == KeyType.Character){

            char c =
                    Character.toLowerCase(
                            key.getCharacter());

            switch(c){

                case 'w' ->
                        presenter.moveUp();

                case 's' ->
                        presenter.moveDown();

                case 'a' ->
                        presenter.moveLeft();

                case 'd' ->
                        presenter.moveRight();

                case 'h' ->
                        presenter.beginWeaponSelection();

                case 'j' ->
                        presenter.beginFoodSelection();

                case 'k' ->
                        presenter.beginElixirSelection();

                case 'e' ->
                        presenter.beginScrollSelection();

                case 't' ->
                        presenter.toggleStatsView();
            }
        }
    }

    // ----------------------------------------------------------------
    // Menu input routing
    // ----------------------------------------------------------------

    private void handleMenuInput(KeyStroke key){

        switch(key.getKeyType()){

            case ArrowUp ->
                    presenter.menuUp();

            case ArrowDown ->
                    presenter.menuDown();

            case Enter ->
                    presenter.menuSelect();

            default -> {
                // ignore other keys in menu
            }
        }
    }

}

