Feature: Create new room
  @nominal
  Scenario: Create room and add player
    # Create Room
    Given url 'http://localhost:8080/rooms'
    And request { name: 'player1'}
    When method POST
    Then status 200
    And def roomId = response._id

    # Start a game
    Given url 'http://localhost:8080/rooms/'+roomId+'/games'
    And request {}
    When method POST
    Then status 200

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200
    And match response == { first: {roll:#number}, second: {roll:#number} }

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 0,"y": 0}
    When method POST
    Then status 200
