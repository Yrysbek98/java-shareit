package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    UserService userService;

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable int id){
        return userService.getUserById(id);
    }

    @GetMapping()
    public List<UserDto> getAllUsers(){
       return userService.getAllUsers();
    }

    @PostMapping()
    public UserDto addNewUser(@RequestBody UserDto userDto){
        return userService.addNewUser(userDto);
    }

    @PatchMapping
    public UserDto updateUser(@RequestBody UserDto userDto){
        return userService.updateUser(userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id){
        userService.deleteUserById(id);
    }

}
