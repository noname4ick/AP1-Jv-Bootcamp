package com.rogue.presentation.view;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.rogue.domain.game.GameSession;
import com.rogue.domain.game.GameState;
import com.rogue.presentation.input.InputHandler;
import com.rogue.presentation.presenter.GamePresenter;

import java.io.IOException;

public class LanternaGameView implements GameView{

    private Screen screen;

    private GamePresenter presenter;

    private Renderer renderer;

    private InputHandler input;

    public LanternaGameView(
            GamePresenter presenter){

        this.presenter = presenter;
    }

    public void start(){

        try{

            Terminal terminal =
                    new DefaultTerminalFactory()
                            .setInitialTerminalSize(
                                    new TerminalSize(
                                            ScreenLayout.TERMINAL_COLS,
                                            ScreenLayout.TERMINAL_ROWS))
                            .createTerminal();

            screen =
                    new TerminalScreen(terminal);

            screen.startScreen();

            renderer =
                    new Renderer(screen);

            input =
                    new InputHandler(
                            presenter,
                            screen);

            gameLoop();

        }catch(IOException e){

            e.printStackTrace();
        }
    }

    private void gameLoop()
            throws IOException{

        while(true){

            GameState state =
                    presenter.getSession().getState();

            if(state == GameState.MENU
                    || state == GameState.GAME_OVER
                    || state == GameState.VICTORY){

                presenter.update();

                KeyStroke key = screen.readInput();

                if(key != null){

                    input.processKey(key);
                }

                screen.refresh();

            } else {

                presenter.update();

                input.process();

                screen.refresh();

                try{

                    Thread.sleep(16);

                }catch(InterruptedException ignored){

                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private GameState lastRenderedState = null;

    public void render(
            GameSession session){

        GameState state = session.getState();

        if(state != lastRenderedState){

            screen.clear();

            lastRenderedState = state;
        }

        if(state == GameState.MENU){

            renderer.drawMenu(
                    presenter.getMenu());

            return;
        }

        renderer.draw(
                session,
                presenter.isStatsView());
    }

}

