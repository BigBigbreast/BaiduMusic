package com.example.administrator.baidumusic.Service;

/**
 * @author Admin
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public interface IBinder {
    public void play();
    public void pause();
    public void restart();
    public void callseekto(int position);
}
