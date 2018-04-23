package com.codingame.gameengine.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.util.Types;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Type;

public class RefereeMain {
    
    private static boolean inProduction = false;
    
    public static boolean isInProduction() {
        return inProduction;
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        inProduction = true;
        InputStream in = System.in;
        PrintStream out = System.out;
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // Do nothing.
            }
        }));
        System.setIn(new InputStream() {
            @Override
            public int read() throws IOException {
                throw new RuntimeException("Impossible to read from the referee");
            }
        });
        start(in, out);
    }

    @SuppressWarnings("unchecked")
    public static void start(InputStream is, PrintStream out) {
        start(is, out, "com.codingame.game");
    }

    @SuppressWarnings("unchecked")
    public static void start(InputStream is, PrintStream out, String refereePackagePrefix) {
        GameEngineModule gameEngineModule = new GameEngineModule();
        gameEngineModule.setClassPathPrefix(refereePackagePrefix);
        Injector injector = Guice.createInjector(gameEngineModule);

        Type type = Types.newParameterizedType(GameManager.class, AbstractPlayer.class);
        GameManager<AbstractPlayer> gameManager = (GameManager<AbstractPlayer>) injector.getInstance(Key.get(type));

        gameManager.start(is, out);
    }
}