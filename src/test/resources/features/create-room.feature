Feature: Create new room
  @nominal
  Scenario: Create room and add player
    # Create Room
    Given url 'http://localhost:8080/rooms'
    And request { name: 'toto'}
    When method POST
    Then status 200
    And def roomId = response._id

    # Add existing player
    Given url 'http://localhost:8080/rooms/'+roomId+'/players'
    And request { name: 'toto'}
    When method POST
    Then status 409

    # Add non existing player
    Given url 'http://localhost:8080/rooms/'+roomId+'/players'
    And request { name: 'tutu'}
    When method POST
    Then status 200
