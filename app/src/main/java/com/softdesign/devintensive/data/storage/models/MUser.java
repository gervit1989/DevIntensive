package com.softdesign.devintensive.data.storage.models;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * Структура данных пользователя
 */
@Entity(active = true, nameInDb = "USER")
public class MUser {

    /**
     * Номер в базе
     */
    @Id
    private Long id;

    /**
     * Уникальный номер на сервере
     */
    @NotNull
    @Unique
    private String remoteId;


    /**
     * Полное имя пользователя
     */
    @NotNull
    @Unique
    private String fullName;


    /**
     * Фото пользователя
     */
    private String photo;

    /**
     * Аватар пользователя
     */
    private String avatar;

    /**
     * Количество строк кода
     */
    private int codelines;

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 793142862)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMUserDao() : null;
    }

    /** Used for active entity operations. */
    @Generated(hash = 790408396)
    private transient MUserDao myDao;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public String getAvatar() {
        return this.avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRemoteId() {
        return this.remoteId;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCodelines() {
        return this.codelines;
    }

    public void setCodelines(int codelines) {
        this.codelines = codelines;
    }

    @Generated(hash = 1011763961)
    public MUser(Long id, @NotNull String remoteId, @NotNull String fullName, String photo,
            String avatar, int codelines) {
        this.id = id;
        this.remoteId = remoteId;
        this.fullName = fullName;
        this.photo = photo;
        this.avatar = avatar;
        this.codelines = codelines;
    }

    @Generated(hash = 916565968)
    public MUser() {
    }

}
