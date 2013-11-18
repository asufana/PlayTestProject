package playddd.util;

import play.db.jpa.*;

/** トランザクション */
public class Transaction {
    
    /** 現トランザクションの退避 */
    private JPA currentTransaction = null;
    
    /** トランザクション開始 */
    public void open() {
        if (isOpened()) {
            return;
        }
        // 既存トランザクションを待避
        currentTransaction = JPA.local.get();
        // localトランザクションを初期化
        JPA.local.remove();
        // 新規トランザクションを開始
        JPAPlugin.startTx(false);
    }
    
    /** コミット */
    public void commit() {
        close(false);
    }
    
    /** ロールバック */
    public void rollback() {
        close(true);
    }
    
    /** トランザクションの開始有無 */
    public boolean isOpened() {
        return currentTransaction == null
                ? false
                : true;
    }
    
    /**
     * クローズ
     * @param needRollback true:ロールバック
     */
    private void close(final boolean needRollback) {
        if (isOpened() == false) {
            throw new RuntimeException("トランザクションが開始されていません。");
        }
        // クローズ
        JPAPlugin.closeTx(needRollback);
        // 既存のトランザクションに復帰
        JPA.local.set(currentTransaction);
        // 退避領域を初期化
        currentTransaction = null;
    }
}
