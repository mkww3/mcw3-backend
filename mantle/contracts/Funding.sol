// SPDX-License-Identifier: GPL-3.0
pragma solidity ^0.8.0;

import "./IFunding.sol";

contract Funding is IFunding {
    string public name;
    string public description;
    uint256 public totalCotributtion;

    address payable public owner;
    mapping (address => uint256) public contributions;
    address[] public contributors;
    
    
    struct Contributor {
        address contributor;
        uint256 contribution;
    }
    
    constructor(string memory _name, string memory _description, address _owner) {
        name = _name;
        description = _description;
        owner = payable(_owner); 
    }

    function getName() public view returns (string memory){
        return name;
    }
    function getDescription() public view returns (string memory){
        return description;
    }
    function getTotalCotributtion() public view returns(uint256){
        return totalCotributtion;
    }


    function fund() public payable {
        if(contributions[msg.sender] == 0) {
            contributors.push(msg.sender);
        }
        contributions[msg.sender] += msg.value;
        totalCotributtion+= msg.value;
    }

    function withdraw() public onlyOwner {
        uint256 balance = address(this).balance;
        owner.transfer(balance);
    }

    function listContributions() public view returns(Contributor[] memory)  {
        Contributor[] memory list = new Contributor[](contributors.length);
        for(uint256 i = 0 ; i < list.length; i++) {
            list[i] = Contributor({
                contributor: contributors[i],
                contribution: contributions[contributors[i]]
            });
        }
        return list;
    }

    modifier onlyOwner() {
        require(owner == msg.sender, "Called is not owner");
        _;
    }
}