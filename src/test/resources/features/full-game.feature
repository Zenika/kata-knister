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

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 0,"y": 0}
    When method POST
    Then status 200

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 0,"y": 1}
    When method POST
    Then status 200

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 0,"y": 2}
    When method POST
    Then status 200

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 0,"y": 3}
    When method POST
    Then status 200

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 0,"y": 4}
    When method POST
    Then status 200

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 1,"y": 0}
    When method POST
    Then status 200

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 1,"y": 1}
    When method POST
    Then status 200

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 1,"y": 2}
    When method POST
    Then status 200

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 1,"y": 3}
    When method POST
    Then status 200

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 1,"y": 4}
    When method POST
    Then status 200

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 2,"y": 0}
    When method POST
    Then status 200

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 2,"y": 1}
    When method POST
    Then status 200

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 2,"y": 2}
    When method POST
    Then status 200

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 2,"y": 3}
    When method POST
    Then status 200

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 2,"y": 4}
    When method POST
    Then status 200

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 3,"y": 0}
    When method POST
    Then status 200

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 3,"y": 1}
    When method POST
    Then status 200

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 3,"y": 2}
    When method POST
    Then status 200

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 3,"y": 3}
    When method POST
    Then status 200

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 3,"y": 4}
    When method POST
    Then status 200

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 4,"y": 0}
    When method POST
    Then status 200

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 4,"y": 1}
    When method POST
    Then status 200

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 4,"y": 2}
    When method POST
    Then status 200

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 4,"y": 3}
    When method POST
    Then status 200

    # Roll dices
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/roll'
    And request {}
    When method POST
    Then status 200

    # Place dice roll
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/player1/grid'
    And request {"x": 4,"y": 4}
    When method POST
    Then status 200

    # Get scores
    Given url 'http://localhost:8080/rooms/'+roomId+'/games/scores'
    When method GET
    Then status 200
