/*
* This is the source code of iGap for Android
* It is licensed under GNU AGPL v3.0
* You should have received a copy of the license in this archive (see LICENSE).
* Copyright © 2017 , iGap - www.iGap.net
* iGap Messenger | Free, Fast and Secure instant messaging application
* The idea of the RooyeKhat Media Company - www.RooyeKhat.co
* All rights reserved.
*/

package net.iGap.realm;

import io.realm.Realm;
import io.realm.RealmObject;
import java.util.ArrayList;
import java.util.List;
import net.iGap.module.SerializationUtils;
import net.iGap.module.TimeUtils;
import net.iGap.proto.ProtoGlobal;

public class RealmWallpaper extends RealmObject {

    private long lastTimeGetList;
    private byte[] wallPaperList;
    private byte[] localList;

    public List<ProtoGlobal.Wallpaper> getWallPaperList() {
        return wallPaperList == null ? null : (List<ProtoGlobal.Wallpaper>) SerializationUtils.deserialize(wallPaperList);
    }

    public void setWallPaperList(List<ProtoGlobal.Wallpaper> wallpaperListProto) {
        this.wallPaperList = SerializationUtils.serialize(wallpaperListProto);
    }

    public ArrayList<String> getLocalList() {
        return localList == null ? null : ((ArrayList<String>) SerializationUtils.deserialize(localList));
    }

    public void setLocalList(ArrayList<String> list) {
        this.localList = SerializationUtils.serialize(list);
    }

    public long getLastTimeGetList() {
        return lastTimeGetList;
    }

    public void setLastTimeGetList(long lastTimeGetList) {
        this.lastTimeGetList = lastTimeGetList;
    }

    public static void updateField(final List<ProtoGlobal.Wallpaper> protoList, final String lockaPath) {

        Realm realm = Realm.getDefaultInstance();

        final RealmWallpaper realmWallpaper = realm.where(RealmWallpaper.class).findFirst();

        realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {

                RealmWallpaper item;

                if (realmWallpaper == null) {
                    final RealmWallpaper rw = new RealmWallpaper();
                    item = realm.copyToRealm(rw);
                } else {
                    item = realmWallpaper;
                }

                if (protoList != null) {
                    item.setWallPaperList(protoList);
                    item.setLastTimeGetList(TimeUtils.currentLocalTime());
                }

                if (lockaPath.length() > 0) {

                    ArrayList<String> lockalList = item.getLocalList();

                    if (lockalList == null) {

                        lockalList = new ArrayList<String>();
                        lockalList.add(lockaPath);
                        item.setLocalList(lockalList);
                    } else if (lockalList.indexOf(lockaPath) == -1) {
                        lockalList.add(0, lockaPath);
                        item.setLocalList(lockalList);
                    }
                }
            }
        });

        realm.close();
    }




}