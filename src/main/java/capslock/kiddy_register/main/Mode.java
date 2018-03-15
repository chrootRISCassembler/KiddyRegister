/*
    Copyright (C) 2018 RISCassembler

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

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
                constListBuilder.add(State.REGISTER_CONTENT);

                if (ordinal() == 0)constListBuilder.add(State.REGISTER_GAME_ID);

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
