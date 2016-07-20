package com.softdesign.devintensive.data.storage.models;

import com.softdesign.devintensive.data.network.res.UserListResponse;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

import java.util.List;

/**
 * Структура данных пользователя
 */
@Entity(active = true, nameInDb = "USERS")
public class User {

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
     * Имя пользователя
     */
    @NotNull
    @Unique
    private String name;
    /**
     * Фамилия пользователя
     */
    @NotNull
    @Unique
    private String family;


    /**
     * Фото пользователя
     */
    private String photo;

    /**
     * Полное имя пользователя для поиска
     */
    @NotNull
    @Unique
    private String searchName;

    /**
     * О себе
     */
    private String bio;

    /**
     * Количество проектов
     */
    private int projects;

    /**
     * Рейтинг
     */
    private int rating;

    /**
     * Количество строк кода
     */
    private int codelines;

    /**
     * Список репозиториев пользователя
     */
    @ToMany(joinProperties = {
            @JoinProperty(name = "remoteId", referencedName = "userRemoteId")
    })
    List<Repository> repositories;

    public User(UserListResponse.UserData userRes) {
        this.remoteId = userRes.getId();
        this.bio = userRes.getPublicInfo().getBio();
        this.rating = userRes.getProfileValues().getRating();
        this.codelines = userRes.getProfileValues().getLinesCode();
        this.projects = userRes.getProfileValues().getProjectCount();
        this.photo = userRes.getPublicInfo().getPhoto();
        this.fullName = userRes.getFirstName() + ' ' + userRes.getSecondName();
        this.searchName = this.fullName.toUpperCase();
    }

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
    @Generated(hash = 2059241980)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }

    /** Used for active entity operations. */
    @Generated(hash = 1507654846)
    private transient UserDao myDao;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public int getCodelines() {
        return this.codelines;
    }

    public void setCodelines(int codelines) {
        this.codelines = codelines;
    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getProjectCount() {
        return this.projects;
    }

    public void setProjectCount(int projects) {
        this.projects = projects;
    }

    public String getBio() {
        return this.bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSearchName() {
        return this.searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
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

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 438307964)
    public synchronized void resetRepositories() {
        repositories = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1643807649)
    public List<Repository> getRepositories() {
        if (repositories == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RepositoryDao targetDao = daoSession.getRepositoryDao();
            List<Repository> repositoriesNew = targetDao._queryUser_Repositories(remoteId);
            synchronized (this) {
                if(repositories == null) {
                    repositories = repositoriesNew;
                }
            }
        }
        return repositories;
    }

    public int getProjects() {
        return this.projects;
    }

    public void setProjects(int projects) {
        this.projects = projects;
    }

    public String getFamily() {
        return this.family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Generated(hash = 583188955)
    public User(Long id, @NotNull String remoteId, @NotNull String fullName, @NotNull String name,
            @NotNull String family, String photo, @NotNull String searchName, String bio, int projects,
            int rating, int codelines) {
        this.id = id;
        this.remoteId = remoteId;
        this.fullName = fullName;
        this.name = name;
        this.family = family;
        this.photo = photo;
        this.searchName = searchName;
        this.bio = bio;
        this.projects = projects;
        this.rating = rating;
        this.codelines = codelines;
    }

    @Generated(hash = 586692638)
    public User() {
    }
}
