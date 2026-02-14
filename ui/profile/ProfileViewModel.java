package com.example.smartfoods.ui.profile;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartfoods.data.entities.Profile;
import com.example.smartfoods.data.repository.ProfileRepository;

import java.util.List;

public class ProfileViewModel extends AndroidViewModel {
    private ProfileRepository repository;
    private LiveData<List<Profile>> allProfiles;
    private MutableLiveData<Profile> currentProfile = new MutableLiveData<>();

    public ProfileViewModel(Application application) {
        super(application);
        repository = new ProfileRepository(application);
        allProfiles = repository.getAllProfiles();
        
        // Load the primary profile if it exists
        loadPrimaryProfile();
    }

    public void insert(Profile profile) {
        repository.insert(profile);
    }

    public void update(Profile profile) {
        repository.update(profile);
    }

    public void delete(Profile profile) {
        repository.delete(profile);
    }

    public LiveData<List<Profile>> getAllProfiles() {
        return allProfiles;
    }

    public LiveData<Profile> getCurrentProfile() {
        return currentProfile;
    }

    public void loadPrimaryProfile() {
        Profile profile = repository.getPrimaryProfile();
        if (profile != null) {
            currentProfile.setValue(profile);
        }
    }

    public void saveProfile(String displayName, String sport, String competitionStatus, String competitionDate) {
        Profile profile = currentProfile.getValue();
        if (profile == null) {
            // Create new profile if it doesn't exist
            profile = new Profile(displayName, sport, competitionStatus, competitionDate);
            insert(profile);
        } else {
            // Update existing profile
            profile.displayName = displayName;
            profile.sport = sport;
            profile.competitionStatus = competitionStatus;
            profile.nextCompetitionDate = competitionDate;
            update(profile);
        }
        currentProfile.setValue(profile);
    }
}
