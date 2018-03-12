package capslock.kiddy_register.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

enum Mode {
    /**
     * ゲーム情報を新規登録する.
     */
    REGISTER,
    /**
     * 登録済みゲーム情報を更新する.
     */
    UPDATE,
    /**
     * ランチャーでの表示を確かめる.
     */
    PREVIEW;

    private final List<State> stateList;

    Mode(){
        final List<State> constListBuilder = new ArrayList<>();

        switch (ordinal()){
            case 0:
            case 1:
                constListBuilder.add(State.SET_GAME_ROOT_DIR);
                constListBuilder.add(State.REGISTER_EXE);
                constListBuilder.add(State.REGISTER_NAME);
                constListBuilder.add(State.REGISTER_DESC);
                constListBuilder.add(State.REGISTER_PANEL);
                constListBuilder.add(State.REGISTER_IMAGE);
                constListBuilder.add(State.REGISTER_MOVIE);

                //switch (this){//if-statementが何故か使えない
                //    case REGISTER:
                //        constListBuilder.add(State.REGISTER_GAME_ID);
                //}

                constListBuilder.add(State.WRITE_JSON);
            case 2: //Fall through
                constListBuilder.add(State.PREVIEW_LAUNCHER);
        }

        stateList = Collections.unmodifiableList(constListBuilder);
    }

    public List<State> getStateList() {
        return stateList;
    }
}
