package jp.co.aforce.tool;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import jp.co.aforce.beans.Lot;
import jp.co.aforce.dao.LotDAO;

public class AgingEngine {
    private LotDAO lotDAO = new LotDAO();

    // 熟成状態を更新するメイン処理
    public void processAging() {
        try {
            // 1. まだ廃棄されていない全ロットを取得
            List<Lot> activeLots = lotDAO.getAllActiveLots(); 

            for (Lot lot : activeLots) {
                long days = ChronoUnit.DAYS.between(lot.getArrivalDate(), LocalDateTime.now());

                // 2. 熟成・廃棄ルールの判定
                if (days >= 30) {
                    lotDAO.updateSpecificLotStatus(lot.getLotId(), 2); // 2: 廃棄
                } else if (days >= 7) {
                    lotDAO.updateSpecificLotStatus(lot.getLotId(), 1); // 1: 熟成
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}