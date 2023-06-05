// SPDX-License-Identifier: GPL-3.0
pragma solidity ^0.8.0;

import "./Funding.sol";
import "./IFunding.sol";
import "./FundingData.sol";

contract FundingFactory {
    address[] public fundings;

    function createFunding(
        string calldata name,
        string calldata description
    ) public {
        Funding funding = new Funding(
            name,
            description,
            msg.sender
        );
        fundings.push(address(funding));
    }

    function listFundings() public view returns(FundingData[] memory) {
        FundingData[] memory list = new FundingData[](fundings.length);
        for (uint256 i = 0; i < list.length ; i++) {
            IFunding funding = IFunding(fundings[i]);
            list[i] = FundingData({
                contractAddress: address(funding),
                name: funding.getName(),
                description: funding.getDescription(),
                totalCotributtion: funding.getTotalCotributtion()
            });
        }
        return list;
    }
}