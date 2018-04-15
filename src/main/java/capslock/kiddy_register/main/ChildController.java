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

import methg.commonlib.trivial_logger.Logger;

abstract class ChildController {
    static MainController parentController;

    static void setParentController(MainController controller){
        parentController = controller;
    }

    /**
     * 遷移した時,最初に呼び出される.
     */
    void init(){
        Logger.INST.debug( Thread.currentThread().getStackTrace()[1].getMethodName() + " called");
    }

    /**
     * 別の表示状態に遷移するとき,つまり見えなくなるときに呼び出される.
     */
    void transition() {
        Logger.INST.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " called");
    }
}
