// SPDX-License-Identifier: GPL-3.0
pragma solidity ^0.8.0;

interface IFunding {
    function getName() external view returns (string memory);
    function getDescription() external view returns (string memory);
    function getTotalCotributtion() external view returns(uint256);
}