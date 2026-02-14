package com.example.smartfoods.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.smartfoods.data.AppDatabase;
import com.example.smartfoods.data.dao.ProfileDao;
import com.example.smartfoods.data.entities.Profile;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ProfileRepository {
    private ProfileDao profileDao;
    private LiveData<List<Profile>> allProfiles;

    public ProfileRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        profileDao = database.profileDao();
        allProfiles = profileDao.getAll();
    }

    public void insert(Profile profile) {
        new InsertProfileAsyncTask(profileDao).execute(profile);
    }

    public void update(Profile profile) {
        new UpdateProfileAsyncTask(profileDao).execute(profile);
    }

    public void delete(Profile profile) {
        new DeleteProfileAsyncTask(profileDao).execute(profile);
    }

    public LiveData<List<Profile>> getAllProfiles() {
        return allProfiles;
    }

    public Profile getPrimaryProfile() {
        try {
            return new GetPrimaryProfileAsyncTask(profileDao).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class InsertProfileAsyncTask extends AsyncTask<Profile, Void, Void> {
        private ProfileDao profileDao;

        private InsertProfileAsyncTask(ProfileDao profileDao) {
            this.profileDao = profileDao;
        }

        @Override
        protected Void doInBackground(Profile... profiles) {
            profileDao.insert(profiles[0]);
            return null;
        }
    }

    private static class UpdateProfileAsyncTask extends AsyncTask<Profile, Void, Void> {
        private ProfileDao profileDao;

        private UpdateProfileAsyncTask(ProfileDao profileDao) {
            this.profileDao = profileDao;
        }

        @Override
        protected Void doInBackground(Profile... profiles) {
            profileDao.update(profiles[0]);
            return null;
        }
    }

    private static class DeleteProfileAsyncTask extends AsyncTask<Profile, Void, Void> {
        private ProfileDao profileDao;

        private DeleteProfileAsyncTask(ProfileDao profileDao) {
            this.profileDao = profileDao;
        }

        @Override
        protected Void doInBackground(Profile... profiles) {
            profileDao.delete(profiles[0]);
            return null;
        }
    }

    private static class GetPrimaryProfileAsyncTask extends AsyncTask<Void, Void, Profile> {
        private ProfileDao profileDao;

        private GetPrimaryProfileAsyncTask(ProfileDao profileDao) {
            this.profileDao = profileDao;
        }

        @Override
        protected Profile doInBackground(Void... voids) {
            return profileDao.getPrimaryProfile();
        }
    }
}
