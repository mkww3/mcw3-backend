// SPDX-License-Identifier: GPL-3.0
pragma solidity ^0.8.0;

contract ProjectGate {
    struct Project {
      string id;
      ProjectStatus status;  
    }

    enum ProjectStatus {
        VERIFIED,
        NOT_VERIFIED,
        IN_PROGRESS
    }

    mapping (string => Project) public projects;
    mapping (address => bool) public moderator;
    string[] public projectId;
    uint256 public projectSize;

    constructor() {
        moderator[msg.sender] = true;
    }

    function addModerator(address moderatorAccount) public {
        moderator[moderatorAccount] = true;
    }

    function createProject(string calldata id) public moderatorOnly {
        require(keccak256(abi.encodePacked(projects[id].id)) == keccak256(abi.encodePacked("")), "Project already exists");

        projectId.push(id);
        projectSize += 1;
        projects[id] = Project({
            id: id,
            status: ProjectStatus.NOT_VERIFIED
        });
    }

    function updateStatus(string calldata id, ProjectStatus status) public moderatorOnly {
        require(keccak256(abi.encodePacked(projects[id].id)) != keccak256(abi.encodePacked("")), "Project doesn't exists");
        projects[id].status = status;
    }

    function listProjects() public view returns(Project[] memory) {
        Project[] memory projectList = new Project[](projectSize);

        for (uint256 i = 0 ; i < projectSize; i++) {
            projectList[i] = projects[projectId[i]];
        }

        return projectList;
    }

    modifier moderatorOnly {
        require(moderator[msg.sender], "You are not moderator");
        _;
    }
}