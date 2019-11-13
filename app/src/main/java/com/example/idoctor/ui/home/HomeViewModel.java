package com.example.idoctor.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.idoctor.model.User;
import com.example.idoctor.repository.FirebaseDatabaseRepository;
import com.example.idoctor.repository.UserRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {

//    private MutableLiveData<List<User>> mUsers;
//
//    public HomeViewModel() {
//        mUsers = new MutableLiveData<>();
//        readData();
//    }
//
//    public LiveData<List<User>> getText() {
//        return mUsers;
//    }
//
//    public void readData(){
//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//
//        DatabaseReference myRef = database.getReference("User");
//
//        Query lastQuery = myRef.orderByChild("role").equalTo(1).limitToLast(4);
//
//
//        final List<User> users = new ArrayList<>();
//        System.out.println("DMMMMMMMMMMMMMMMMMMMMM");
//        lastQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                System.out.println("CCCCCCCCCCCCCCCCCCCCCCCC");
//                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
//                    System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO"+keyNode);
//                    User user = keyNode.getValue(User.class);
//                    users.add(user);
//                    System.out.println("AAAAAAAAAAA: "+user.getName());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                System.out.println("UUUUUUUUUUUUUUUUUUUUUU"+databaseError.toString());
//            }
//        });
//        mUsers.setValue(users);
//    }
    private MutableLiveData<List<User>> users;
    private UserRepository userRepository = new UserRepository();

    public HomeViewModel() {
        System.out.println("cdmcdm");
    }


    public LiveData<List<User>> getArticles() {
        System.out.println("dmmmmmmmd");
        if (users == null) {
            System.out.println("iiiiiiiii11111111111111iiiiiiiiiii");
            users = new MutableLiveData<>();
            System.out.println("iiiiiiiiiiiiiiiiiiii");
            loadUsers();
        }
        System.out.println("iciciciciiccicicicicicc");
        return users;
    }

    @Override
    protected void onCleared() {
        userRepository.removeListener();
    }

    private void loadUsers() {
        System.out.println("OKOKOKOKOKOKOKOKOKOKOKO");
        userRepository.addListener(new FirebaseDatabaseRepository.FirebaseDatabaseRepositoryCallback<User>() {
            @Override
            public void onSuccess(List<User> result) {
                users.setValue(result);
                System.out.println("ngon ngon");
                System.out.println(users.getValue().get(0).getName());
                System.out.println(users.getValue().get(1).getName());
                System.out.println(users.getValue().get(2).getName());
            }

            @Override
            public void onError(Exception e) {
                users.setValue(null);
            }
        });
    }
}