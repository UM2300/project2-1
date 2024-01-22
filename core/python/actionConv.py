def conversion(action, player):
    pieceType = -1
    place2 = -1

    if action <= 74:
        action_type = 0
        pieceType = (action) % 3
        place1 = action // 3
        if player == "brown":
            pieceType = pieceType + 3

    elif action >= 75 and action <= 76:
        action_type = 1
        place1 = 0
        if (action == 75):
            place2 = 1
        else:
            place2 = place1 + 5

    elif action >= 77 and action <= 85:
        action_type = 1
        actionSimple = action - 77
        place1 = (actionSimple // 3) + 1
        remainder = actionSimple % 3
        if (remainder == 0):
            place2 = place1 - 1
        elif (remainder == 1):
            place2 = place1 + 5
        else:
            place2 = place1 + 1

    elif action >= 86 and action <= 87:
        action_type = 1
        place1 = 4
        if (action == 86):
            place2 = place1 - 1
        else:
            place2 = place1 + 5

    elif (action >= 88 and action <= 90):
        action_type = 1
        place1 = 5
        if (action == 88):
            place2 = place1 - 5
        elif (action == 89):
            place2 = place1 + 1
        else:
            place2 = place1 + 5

    elif (action >= 91 and action <= 102):
        action_type = 1
        actionSimple = action - 91
        place1 = (actionSimple // 4) + 6
        remainder = actionSimple % 4
        if (remainder == 0):
            place2 = place1 - 1
        elif (remainder == 1):
            place2 = place1 - 5
        elif (remainder == 2):
            place2 = place1 + 1
        else:
            place2 = place1 + 5

    elif (action >= 103 and action <= 105):
        action_type = 1
        place1 = 9
        if (action == 103):
            place2 = place1 - 5
        elif (action == 104):
            place2 = place1 - 1
        else:
            place2 = place1 + 5

    elif (action >= 106 and action <= 108):
        action_type = 1
        place1 = 10
        if (action == 106):
            place2 = place1 - 5
        elif (action == 107):
            place2 = place1 + 1
        else:
            place2 = place1 + 5

    elif (action >= 109 and action <= 120):
        action_type = 1
        actionSimple = action - 109
        place1 = (actionSimple // 4) + 11
        remainder = actionSimple % 4
        if (remainder == 0):
            place2 = place1 - 1
        elif (remainder == 1):
            place2 = place1 - 5
        elif (remainder == 2):
            place2 = place1 + 1
        else:
            place2 = place1 + 5

    elif (action >= 121 and action <= 123):
        action_type = 1
        place1 = 14
        if (action == 121):
            place2 = place1 - 5
        elif (action == 122):
            place2 = place1 - 1
        else:
            place2 = place1 + 5

    elif (action >= 124 and action <= 126):
        action_type = 1
        place1 = 15
        if (action == 124):
            place2 = place1 - 5
        elif (action == 125):
            place2 = place1 + 1
        else:
            place2 = place1 + 5

    elif (action >= 127 and action <= 138):
        action_type = 1
        actionSimple = action - 127
        place1 = (actionSimple // 4) + 16
        remainder = actionSimple % 4
        if (remainder == 0):
            place2 = place1 - 1
        elif (remainder == 1):
            place2 = place1 - 5
        elif (remainder == 2):
            place2 = place1 + 1
        else:
            place2 = place1 + 5

    elif (action >= 139 and action <= 141):
        action_type = 1
        place1 = 19
        if (action == 139):
            place2 = place1 - 5
        elif (action == 140):
            place2 = place1 - 1
        else:
            place2 = place1 + 5

    elif action >= 142 and action <= 143:
        action_type = 1
        place1 = 20
        if (action == 142):
            place2 = place1 - 5
        else:
            place2 = place1 + 1

    elif action >= 144 and action <= 152:
        action_type = 1
        actionSimple = action - 144
        place1 = (actionSimple // 3) + 21
        remainder = actionSimple % 3
        if (remainder == 0):
            place2 = place1 - 1
        elif (remainder == 1):
            place2 = place1 - 5
        else:
            place2 = place1 + 1

    elif action >= 153 and action <= 154:
        action_type = 1
        place1 = 24
        if (action == 153):
            place2 = place1 - 5
        else:
            place2 = place1 - 1

    return action_type, pieceType, place1, place2
