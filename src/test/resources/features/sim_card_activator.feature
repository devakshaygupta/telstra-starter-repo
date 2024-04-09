Feature: SIM Card Activation

  Scenario: Successful Activation
    Given the SIM card Actuator is running
    When I submit an activation request with ICCID "1255789453849037777"
    Then the activation should be successful
    And I query the activation status using the endpoint
    And the response confirms the activation success

  Scenario: Failed Activation
    Given the SIM card Actuator is running
    When I submit an activation request with ICCID "8944500102198304826"
    Then the activation should fail
    And I query the activation status using the endpoint
    And the response confirms the activation failure
