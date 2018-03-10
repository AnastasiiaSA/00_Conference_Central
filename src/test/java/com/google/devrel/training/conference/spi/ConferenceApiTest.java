package com.google.devrel.training.conference.spi;

import static com.google.devrel.training.conference.service.OfyService.ofy;
import static org.junit.Assert.*;

import com.google.api.server.spi.response.UnauthorizedException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
// import com.google.devrel.training.conference.domain.Conference;
import com.google.devrel.training.conference.domain.Profile;
// import com.google.devrel.training.conference.form.ConferenceForm;
import com.google.devrel.training.conference.form.ProfileForm;
import com.google.devrel.training.conference.form.ProfileForm.TeeShirtSize;
import com.googlecode.objectify.Key;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Named;


/**
 * Tests for ConferenceApi API methods.
 */
public class ConferenceApiTest {

    private static final String EMAIL = "example@gmail.com";

    private static final String USER_ID = "123456789";

    private static final TeeShirtSize TEE_SHIRT_SIZE = TeeShirtSize.NOT_SPECIFIED;

    private static final String DISPLAY_NAME = "Your Name Here";

    private static final String NAME = "GCP Live";

    private static final String DESCRIPTION = "New announcements for Google Cloud Platform";

    private static final String CITY = "San Francisco";

    private static final int MONTH = 3;

    private static final int CAP = 500;

    private User user;

    private ConferenceApi conferenceApi;

    private final LocalServiceTestHelper helper =
            new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig()
                    .setDefaultHighRepJobPolicyUnappliedJobPercentage(100));

    @Before
    public void setUp() throws Exception {
        helper.setUp();
        user = new User(EMAIL, "gmail.com", USER_ID);
        conferenceApi = new ConferenceApi();
    }

    @After
    public void tearDown() throws Exception {
        ofy().clear();
        helper.tearDown();
    }

    @Test(expected = UnauthorizedException.class)
    public void testGetProfileWithoutUser() throws Exception {
        conferenceApi.getProfile(null);
    }

    @Test
    public void testGetProfileFirstTime() throws Exception {
        Profile profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        assertNull(profile);
        profile = conferenceApi.getProfile(user);
        assertNull(profile);
    }

    @Test
    public void testSaveProfile() throws Exception {
        // Save the profile for the first time.
        Profile profile = conferenceApi.saveProfile(
                user, new ProfileForm(DISPLAY_NAME, TEE_SHIRT_SIZE));
        // Check the return value first.
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertEquals(TEE_SHIRT_SIZE, profile.getTeeShirtSize());
        assertEquals(DISPLAY_NAME, profile.getDisplayName());
        // Fetch the Profile via Objectify.
        profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertEquals(TEE_SHIRT_SIZE, profile.getTeeShirtSize());
        assertEquals(DISPLAY_NAME, profile.getDisplayName());
    }

    @Test
    public void testSaveProfileWithNull() throws Exception {
        // Save the profile for the first time with null values.
        Profile profile = conferenceApi.saveProfile(user, new ProfileForm(null, null));
        String displayName = EMAIL.substring(0, EMAIL.indexOf("@"));
        // Check the return value first.
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertEquals(TEE_SHIRT_SIZE, profile.getTeeShirtSize());
        assertEquals(displayName, profile.getDisplayName());
        // Fetch the Profile via Objectify.
        profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertEquals(TEE_SHIRT_SIZE, profile.getTeeShirtSize());
        assertEquals(displayName, profile.getDisplayName());
    }

    @Test
    public void testGetProfile() throws Exception {
        conferenceApi.saveProfile(user, new ProfileForm(DISPLAY_NAME, TEE_SHIRT_SIZE));
        // Fetch the Profile via the API.
        Profile profile = conferenceApi.getProfile(user);
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertEquals(TEE_SHIRT_SIZE, profile.getTeeShirtSize());
        assertEquals(DISPLAY_NAME, profile.getDisplayName());
    }

    @Test
    public void testUpdateProfile() throws Exception {
        // Save for the first time.
        conferenceApi.saveProfile(user, new ProfileForm(DISPLAY_NAME, TEE_SHIRT_SIZE));
        Profile profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertEquals(TEE_SHIRT_SIZE, profile.getTeeShirtSize());
        assertEquals(DISPLAY_NAME, profile.getDisplayName());
        // Then try to update it.
        String newDisplayName = "New Name";
        TeeShirtSize newTeeShirtSize = TeeShirtSize.L;
        conferenceApi.saveProfile(user, new ProfileForm(newDisplayName, newTeeShirtSize));
        profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertEquals(newTeeShirtSize, profile.getTeeShirtSize());
        assertEquals(newDisplayName, profile.getDisplayName());
    }

    @Test
    public void testUpdateProfileWithNulls() throws Exception {
        conferenceApi.saveProfile(user, new ProfileForm(DISPLAY_NAME, TEE_SHIRT_SIZE));
        // Update the Profile with null values.
        Profile profile = conferenceApi.saveProfile(user, new ProfileForm(null, null));
        // Expected behavior is that the existing properties do not get overwritten

        // Check the return value first.
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertEquals(TEE_SHIRT_SIZE, profile.getTeeShirtSize());
        assertEquals(DISPLAY_NAME, profile.getDisplayName());
        // Fetch the Profile via Objectify.
        profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertEquals(TEE_SHIRT_SIZE, profile.getTeeShirtSize());
        assertEquals(DISPLAY_NAME, profile.getDisplayName());
    }
    private static Profile getProfileFromUser() {
        Profile profile = ofy().load().key(
                Key<Profile> profileKey = Key.create(Profile.class,userId);
    }
    public Conference createConference() {
        Key<Profile> profileKey = Key.create(Profile.class, userId);
        Key<Conference> key = factory().allocatedId(ProfileKey,Conference.Class);
        key.getId();
        Profile profile = getProfileFromUser(user);
        Conference conference = new Conference(conferenceId, userId, conferenceForm);
        ofy().save().entities(Profile, Conference).now();

    }
import java.util.List;
import com.googlecode.objectify.cmd.Query;
    @ApiMethod(
            name = "queryConferences",
            path = "queryConferences",
            httpMethod = HttpMethod.POST
    )
    public List queryConferences(ConferenceQueryForm conferenceQueryForm) {
        Iterable<Conference> conferenceIterable = conferenceQueryForm.getQuery();
        List<Conference> result = new ArrayList<>(0);
        List<Key<Profile>> organizersKeyList = new ArrayList<>(0);
        for (Conference conference : conferenceIterable) {
            organizersKeyList.add(Key.create(Profile.class, conference.getOrganizerUserId()));
            result.add(conference);
        }
        ofy().load().keys(organizersKeyList);
        return result;
    }




    @ApiMethod(
            name = "getConferenceCreated",
            path = "getConferenceCreated",
            httpMethod = httpMethod.POST
    )
    public List<Conference> getConferencesCreated(){
        userId = user.getUserId(Profile.class, userId);
        Key<Profile> userKey = Key.created(Profile.class, userId);
        ofy().load().type(Conference.class)
            .ansestor(userKey)
            .order("name").list();
    }


    @ApiMethod(
            name = "getConferencesFiltered",
            path = "getConferencesFiltered",
            httpMethod = HttpMethod.POST
    )
    public List<Conference> getConferencesFiltered(){
        Query query = ofy().load().type(Conference.class);
        query = query.filter("maxAttendees >",10) query = query.filter("city =", "London");
        query = query.filter("topics =", "Web Technologies");
        query = query.filter("month =", 1) .order("maxAttendees").order("name");
        return query.list();
    }

    /**
     * Returns a Conference object with the given conferenceId.
     *
     * @param websafeConferenceKey The String representation of the Conference Key.
     * @return a Conference object with the given conferenceId.
     * @throws NotFoundException when there is no Conference with the given conferenceId.
     */
    @ApiMethod(
            name = "getConference",
            path = "conference/{websafeConferenceKey}",
            httpMethod = HttpMethod.GET
    )
    public Conference getConference(
            @Named("websafeConferenceKey") final String websafeConferenceKey)
            throws NotFoundException {
        Key<Conference> conferenceKey = Key.create(websafeConferenceKey);
        Conference conference = ofy().load().key(conferenceKey).now();
        if (conference == null) {
            throw new NotFoundException("No Conference found with key: " + websafeConferenceKey);
        }
        return conference;
    }


    /**
     * Just a wrapper for Boolean.
     * We need this wrapped Boolean because endpoints functions must return
     * an object instance, they can't return a Type class such as
     * String or Integer or Boolean
     */
    public static class WrappedBoolean {

        private final Boolean result;
        private final String reason;

        public WrappedBoolean(Boolean result) {
            this.result = result;
            this.reason = "";
        }

        public WrappedBoolean(Boolean result, String reason) {
            this.result = result;
            this.reason = reason;
        }

        public Boolean getResult() {
            return result;
        }

        public String getReason() {
            return reason;
        }
    }

    /**
     * Register to attend the specified Conference.
     *
     * @param user An user who invokes this method, null when the user is not signed in.
     * @param websafeConferenceKey The String representation of the Conference Key.
     * @return Boolean true when success, otherwise false
     * @throws UnauthorizedException when the user is not signed in.
     * @throws NotFoundException when there is no Conference with the given conferenceId.
     */
    @ApiMethod(
            name = "registerForConference",
            path = "conference/{websafeConferenceKey}/registration",
            httpMethod = HttpMethod.POST
    )

    public WrappedBoolean registerForConference_SKELETON(final User user,
                                                         @Named("websafeConferenceKey") final String websafeConferenceKey)
            throws UnauthorizedException, NotFoundException,
            ForbiddenException, ConflictException {
        // If not signed in, throw a 401 error.
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }

        // Get the userId
        final String userId = user.getUserId();

        // TODO
        // Start transaction
        WrappedBoolean result = ofy().transact(new Work<WrappedBoolean>();
        try {

            // TODO
            // Get the conference key -- you can get it from websafeConferenceKey
            // Will throw ForbiddenException if the key cannot be created
            Key<Conference> conferenceKey = Key.create(websafeConferenceKey);

                    // TODO
                    // Get the Conference entity from the datastore
                    Conference conference = ofy().load().key(conferenceKey).now();

            // 404 when there is no Conference with the given conferenceId.
            if (conference == null) {
                return new WrappedBoolean (false,
                        "No Conference found with key: "
                                + websafeConferenceKey);
            }

            // TODO
            // Get the user's Profile entity
            Profile profile = getProfileFromUser(user);

            // Has the user already registered to attend this conference?
            if (profile.getConferenceKeysToAttend().contains(
                    websafeConferenceKey)) {
                return new WrappedBoolean (false, "Already registered");
            } else if (conference.getSeatsAvailable() <= 0) {
                return new WrappedBoolean (false, "No seats available");
            } else {
                // All looks good, go ahead and book the seat

                // TODO
                // Add the websafeConferenceKey to the profile's
                // conferencesToAttend property
                profile.addToConferenceKeysToAttend(websafeConferenceKey);

                // TODO
                // Decrease the conference's seatsAvailable
                // You can use the bookSeats() method on Conference
                conference.bookSeats(1);
                // TODO
                // Save the Conference and Profile entities
                ofy().save().entities(Conference, Profile).now();
                // We are booked!
                return new WrappedBoolean(true, "Registration successful");
            }

        }
        catch (Exception e) {
            return new WrappedBoolean(false, "Unknown exception");
        }
    }
});
        // if result is false
        if (!result.getResult()) {
        if (result.getReason().contains("No Conference found with key")) {
        throw new NotFoundException (result.getReason());
        }
        else if (result.getReason() == "Already registered") {
        throw new ConflictException("You have already registered");
        }
        else if (result.getReason() == "No seats available") {
        throw new ConflictException("There are no seats available");
        }
        else {
        throw new ForbiddenException("Unknown exception");
        }
        }
        return result;
        }


/**
 * Returns a collection of Conference Object that the user is going to attend.
 *
 * @param user An user who invokes this method, null when the user is not signed in.
 * @return a Collection of Conferences that the user is going to attend.
 * @throws UnauthorizedException when the User object is null.
 */
@ApiMethod(
        name = "getConferencesToAttend",
        path = "getConferencesToAttend",
        httpMethod = HttpMethod.GET
)
public Collection<Conference> getConferencesToAttend(final User user)
        throws UnauthorizedException, NotFoundException {
        // If not signed in, throw a 401 error.
        if (user == null) {
        throw new UnauthorizedException("Authorization required");
        }
        // TODO
        // Get the Profile entity for the user
        Profile profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now(); // Change this;
        if (profile == null) {
            throw new NotFoundException("Profile doesn't exist.");
        }

        // TODO
        // Get the value of the profile's conferenceKeysToAttend property
        List<String> keyStringsToAttend = profile.getConferenceKeysToAttend();
        // change this
        List<Key<Conference>> keysToAttend = new ArrayList<>();
            for (String keyString : keyStringsToAttend) {
            keysToAttend.add(Key.<Conference>create(keyString));
        }
        return ofy().load().keys(keysToAttend).values();
        }
        // TODO
        // Iterate over keyStringsToAttend,
        // and return a Collection of the
        // Conference entities that the user has registered to atend


        return null;  // change this
/**
 * Unregister from the specified Conference.     *
 * @param user An user who invokes this method, null when the user is not signed in.
 * @param websafeConferenceKey The String representation of the Conference Key to unregister  from.
 * @return Boolean true when success, otherwise false.
 * @throws UnauthorizedException when the user is not signed in.
 * @throws NotFoundException when there is no Conference with the given conferenceId.
 */
    @ApiMethod(
        name = "unregisterFromConference",
        path = "conference/{websafeConferenceKey}/registration",
        httpMethod = HttpMethod.DELETE)
public WrappedBoolean unregisterFromConference(
final User user,
@Named("websafeConferenceKey") final String websafeConferenceKey
        ) throws UnauthorizedException, NotFoundException, ForbiddenException, ConflictException {
        }
        }

    /*
    @Test
    public void testCreateConference() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = dateFormat.parse("03/25/2014");
        Date endDate = dateFormat.parse("03/26/2014");
        List<String> topics = new ArrayList<>();
        topics.add("Google");
        topics.add("Cloud");
        topics.add("Platform");
        ConferenceForm conferenceForm = new ConferenceForm(
                NAME, DESCRIPTION, topics, CITY, startDate, endDate, CAP);
        Conference conference = conferenceApi.createConference(user, conferenceForm);
        // Check the return value.
        assertEquals(NAME, conference.getName());
        assertEquals(DESCRIPTION, conference.getDescription());
        assertEquals(topics, conference.getTopics());
        assertEquals(USER_ID, conference.getOrganizerGplusId());
        assertEquals(CITY, conference.getCity());
        assertEquals(startDate, conference.getStartDate());
        assertEquals(endDate, conference.getEndDate());
        assertEquals(CAP, conference.getMaxAttendees());
        assertEquals(CAP, conference.getSeatsAvailable());
        assertEquals(MONTH, conference.getMonth());
        // Check if a new Profile is created
        Profile profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(EMAIL, profile.getMainEmail());
        assertEquals(TEE_SHIRT_SIZE, profile.getTeeShirtSize());
        String displayName = EMAIL.substring(0, EMAIL.indexOf("@"));
        assertEquals(displayName, profile.getDisplayName());
    }
    */

    /*
    @Test
    public void testGetConferencesCreated() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = dateFormat.parse("03/25/2014");
        Date endDate = dateFormat.parse("03/26/2014");
        List<String> topics = new ArrayList<>();
        topics.add("Google");
        topics.add("Cloud");
        topics.add("Platform");
        ConferenceForm conferenceForm = new ConferenceForm(
                NAME, DESCRIPTION, topics, CITY, startDate, endDate, CAP);
        Conference conference = conferenceApi.createConference(user, conferenceForm);

        List<Conference> conferencesCreated = conferenceApi.getConferencesCreated(user);
        assertEquals(1, conferencesCreated.size());
        assertTrue("The result should contain a conference",
                conferencesCreated.contains(conference));
    }
    */

    /*
    @Test
    public void testGetConference() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = dateFormat.parse("03/25/2014");
        Date endDate = dateFormat.parse("03/26/2014");
        List<String> topics = new ArrayList<>();
        topics.add("Google");
        topics.add("Cloud");
        topics.add("Platform");
        ConferenceForm conferenceForm = new ConferenceForm(
                NAME, DESCRIPTION, topics, CITY, startDate, endDate, CAP);
        Conference conference = conferenceApi.createConference(user, conferenceForm);
        conference = conferenceApi.getConference(conference.getWebsafeKey());
        // Check the return value.
        assertEquals(NAME, conference.getName());
        assertEquals(DESCRIPTION, conference.getDescription());
        assertEquals(topics, conference.getTopics());
        assertEquals(USER_ID, conference.getOrganizerGplusId());
        assertEquals(CITY, conference.getCity());
        assertEquals(startDate, conference.getStartDate());
        assertEquals(endDate, conference.getEndDate());
        assertEquals(CAP, conference.getMaxAttendees());
        assertEquals(CAP, conference.getSeatsAvailable());
        assertEquals(MONTH, conference.getMonth());
    }
    */

    /*
    @Test
    public void testRegistrations() throws Exception {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date startDate = dateFormat.parse("03/25/2014");
        Date endDate = dateFormat.parse("03/26/2014");
        List<String> topics = new ArrayList<>();
        topics.add("Google");
        topics.add("Cloud");
        topics.add("Platform");
        ConferenceForm conferenceForm = new ConferenceForm(
                NAME, DESCRIPTION, topics, CITY, startDate, endDate, CAP);
        Conference conference = conferenceApi.createConference(user, conferenceForm);
        Long conferenceId = conference.getId();

        // Registration
        Boolean result = conferenceApi.registerForConference(
                user, conference.getWebsafeKey()).getResult();
        conference = conferenceApi.getConference(conference.getWebsafeKey());
        Profile profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        assertTrue("registerForConference should succeed.", result);
        assertEquals(CAP - 1, conference.getSeatsAvailable());
        assertTrue("Profile should have the conferenceId in conferenceIdsToAttend.",
                profile.getConferenceKeysToAttend().contains(conference.getWebsafeKey()));

        // Unregister
        result = conferenceApi.unregisterFromConference(
                user, conference.getWebsafeKey()).getResult();
        conference = conferenceApi.getConference(conference.getWebsafeKey());
        profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        assertTrue("unregisterFromConference should succeed.", result);
        assertEquals(CAP, conference.getSeatsAvailable());
        assertFalse("Profile shouldn't have the conferenceId in conferenceIdsToAttend.",
                profile.getConferenceKeysToAttend().contains(conference.getWebsafeKey()));
    }
    */
}