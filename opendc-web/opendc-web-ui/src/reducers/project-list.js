import { combineReducers } from 'redux'
import { ADD_PROJECT_SUCCEEDED, DELETE_PROJECT_SUCCEEDED } from '../actions/projects'
import { FETCH_AUTHORIZATIONS_OF_CURRENT_USER_SUCCEEDED } from '../actions/users'

export function authorizationsOfCurrentUser(state = [], action) {
    switch (action.type) {
        case FETCH_AUTHORIZATIONS_OF_CURRENT_USER_SUCCEEDED:
            return action.authorizationsOfCurrentUser
        case ADD_PROJECT_SUCCEEDED:
            return [...state, action.authorization]
        case DELETE_PROJECT_SUCCEEDED:
            return state.filter((authorization) => authorization[1] !== action.id)
        default:
            return state
    }
}

export const projectList = combineReducers({ authorizationsOfCurrentUser })
