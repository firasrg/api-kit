/**
 * Framework-independent contracts for the standard operations of a REST resource.
 *
 * <p>Each interface fixes the method name, parameters, and return type of one operation ({@code
 * get}, {@code create}, {@code update}, {@code delete}, {@code search}) so resource implementations
 * stay consistent. Interfaces are independent and can be combined, so read-only and partial-CRUD
 * resources remain natural to implement.
 *
 * <p>These types carry no HTTP semantics: paths, verbs, status codes, and validation are declared
 * on the implementing class in the web integration. Domain actions and PUT/PATCH differences are
 * outside this contract.
 */
package io.github.firasrg.apikit.core.resource;
