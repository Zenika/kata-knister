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

    # Start a game
    Given url 'http://localhost:8080/rooms/'+roomId+'/games'
    And request {}
    When method POST
    Then status 200
    And match response == { cancelled: false, running:true, diceRolls: [], gridsForPlayers: #object }

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200
    And match response == { first: {roll:#number}, second: {roll:#number} }
