// package com.mobisoft.taskmanagement.controller;

// import com.mobisoft.taskmanagement.dto.GroupDTO;
// import com.mobisoft.taskmanagement.dto.BaseResponse;
// import com.mobisoft.taskmanagement.service.GroupService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.validation.annotation.Validated;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @Validated
// @RequestMapping("/api/v1/groups")
// public class GroupController {

//     @Autowired
//     private GroupService groupService;

//     @PostMapping
//     public ResponseEntity<BaseResponse<GroupDTO>> createGroup(@Validated @RequestBody GroupDTO groupDTO) {
//         GroupDTO createdGroup = groupService.createGroup(groupDTO);
//         BaseResponse<GroupDTO> response = new BaseResponse<>(201, "Groupe créé avec succès", createdGroup);
//         return new ResponseEntity<>(response, HttpStatus.CREATED);
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<BaseResponse<GroupDTO>> getGroupById(@PathVariable Long id) {
//         GroupDTO group = groupService.getGroupById(id);
//         BaseResponse<GroupDTO> response = new BaseResponse<>(200, "Détails du groupe", group);
//         return new ResponseEntity<>(response, HttpStatus.OK);
//     }

//     @GetMapping("/getAllGroups")
//     public ResponseEntity<BaseResponse<List<GroupDTO>>> getAllGroups() {
//         List<GroupDTO> groups = groupService.findAllGroups();
//         BaseResponse<List<GroupDTO>> response = new BaseResponse<>(200, "Liste des groupes", groups);
//         return new ResponseEntity<>(response, HttpStatus.OK);
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<BaseResponse<GroupDTO>> updateGroup(@PathVariable Long id,@Validated @RequestBody GroupDTO groupDTO) {
//         GroupDTO updatedGroup = groupService.updateGroup(id, groupDTO);
//         BaseResponse<GroupDTO> response = new BaseResponse<>(200, "Groupe mis à jour avec succès", updatedGroup);
//         return new ResponseEntity<>(response, HttpStatus.OK);
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<BaseResponse<Void>> deleteGroup(@PathVariable Long id) {
//         groupService.deleteGroup(id);
//         BaseResponse<Void> response = new BaseResponse<>(200, "Groupe supprimé avec succès", null);
//         return new ResponseEntity<>(response, HttpStatus.OK);
//     }
// }
